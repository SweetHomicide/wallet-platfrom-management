package com.wallet.platform.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.wallet.platform.cfos.CfosRpc;
import com.wallet.platform.cfos.exception.ConnectException;
import com.wallet.platform.cfos.result.Address;
import com.wallet.platform.cfos.result.Block;
import com.wallet.platform.cfos.result.TransactionDetail;
import com.wallet.platform.po.Asset;
import com.wallet.platform.po.SendAssetLog;
import com.wallet.platform.po.Transaction;
import com.wallet.platform.po.WithdrawData;
import com.wallet.platform.service.IAssetService;
import com.wallet.platform.service.ISendAssetLogService;
import com.wallet.platform.service.ITransactionService;
import com.wallet.platform.service.IWithdrawDataService;
import com.wallet.platform.util.PlatfromClient;
import com.wallet.platform.util.Utils;
import com.wallet.platfrom.sdk.beans.OutputBean;
import com.wallet.platfrom.sdk.beans.WithdrawDataBean;

@Component
@DisallowConcurrentExecution
public class DataPollingTask {

	/**
	 * 新地址获取及推送定时任务
	 */
	@Scheduled(initialDelay = 10000, fixedDelay = 30000)
	public void runNewAddress() {
		LOGGER.debug("地址数据轮询任务开始......");
		try {
			List<Asset> assets = assetService.queryAllInuse();
			if (null != assets && assets.size() > 0) {
				StringBuffer buf = new StringBuffer();
				String symbol = null;
				for (int i = 0; i < assets.size(); ++i) {
					try {
						symbol = assets.get(i).getSymbol();
						buf.setLength(0);
						buf.append("symbol=").append(symbol).append("&addresses=");
						OutputBean output = PlatfromClient.getOutput("newaddress", buf.toString());
						LOGGER.info("symbol <== " + symbol);
						while (null != output) {
							Integer numbers = 0;
							if (!Utils.isEmpty(output.getMessage())) {
								try {
									numbers = Integer.parseInt(output.getMessage());
								} catch (Exception e) {
									LOGGER.error("转化字符串为Integer失败，字符串 <== " + output.getMessage(), e);
								}
							}
							LOGGER.info("numbers <== " + numbers);
							if (numbers.intValue() == 0) {
								break;
							}

							output = null;

							String addresses = getAddresses(assets.get(i), numbers);
							if (!Utils.isEmpty(addresses)) {
								buf.setLength(0);
								buf.append("symbol=").append(symbol).append("&addresses=");
								buf.append(addresses);
								output = PlatfromClient.getOutput("newaddress", buf.toString());
								if (null == output) {
									ADDR_MAP.put(symbol, addresses);
								} else {
									ADDR_MAP.remove(symbol);
								}
							}
						}
					} catch (Exception e) {
						LOGGER.error("asset test new address error. symbol <== " + symbol, e);
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error("地址数据轮询任务异常。", e);
		} finally {
			LOGGER.debug("地址数据轮询任务结束......");
		}
	}

	/**
	 * 获取提币数据定时任务
	 */
	@Scheduled(initialDelay = 20000, fixedDelay = 60000)
	public void runGetWithdrawData() {
		LOGGER.debug("提币数据下载处理开始......");
		try {
			String data = "time=" + System.currentTimeMillis();
			OutputBean output = PlatfromClient.getOutput("getwithdrawdata", data);
			if (null != output) {
				List<WithdrawDataBean> dataBeans = JSON.parseArray(output.getMessage(), WithdrawDataBean.class);
				if (null != dataBeans && dataBeans.size() > 0) {
					List<WithdrawData> datas = new ArrayList<WithdrawData>();
					for (int i = 0; i < dataBeans.size(); ++i) {
						WithdrawData withdrawData = new WithdrawData();
						withdrawData.setId(Utils.uuid());
						withdrawData.setAddress(dataBeans.get(i).getAddress());
						withdrawData.setAmount(dataBeans.get(i).getAmount());
						withdrawData.setSymbol(dataBeans.get(i).getSymbol());
						withdrawData.setSerno(dataBeans.get(i).getSerno());
						withdrawData.setIsProcessed(false);
						withdrawData.setIsSend(true);
						withdrawData.setIsSuccess(false);
						withdrawData.setIsUpload(false);
						withdrawData.setProcessTime(System.currentTimeMillis());
						datas.add(withdrawData);
					}
					withdrawDataService.save(datas);
				}
			}
		} catch (Exception e) {
			LOGGER.error("提币数据下载处理异常。", e);
		} finally {
			LOGGER.debug("提币数据下载处理结束......");
		}
	}

	/**
	 * 提币数据处理结果上传任务
	 */
	@Scheduled(initialDelay = 13000, fixedDelay = 2000)
	public void runWithdrawDataResultUpload() {
		LOGGER.debug("提币数据发送上传处理开始......");
		WithdrawData withdrawData = withdrawDataService.getFirstOne();
		try {
			if (null == withdrawData) {
				LOGGER.debug("no withdraw data need process......");
				return;
			}
			if (withdrawData.getIsSend()) { // 需要发送
				
				WithdrawData updateData = new WithdrawData();
				updateData.setId(withdrawData.getId());
				updateData.setIsSend(true);
				
				Asset asset = assetService.getBySymbol(withdrawData.getSymbol());
				if (null == asset) {
					LOGGER.error("asset not found. symbol <== " + withdrawData.getSymbol());
					updateData.setIsSend(false);
					updateData.setIsProcessed(true);
					updateData.setMessage("资产信息不存在");
					updateData.setProcessTime(System.currentTimeMillis());
					withdrawDataService.updateById(updateData);
					return;
				}
				
				if (null == asset.getIsUse() || !asset.getIsUse()) {
					LOGGER.error("asset not use. symbol <== " + withdrawData.getSymbol());
					updateData.setMessage("资产信息使用未开启");
					updateData.setProcessTime(System.currentTimeMillis());
					withdrawDataService.updateById(updateData);
					return;
				}
				
				CfosRpc cfosRpc = CfosRpc.getRpc(asset.getCfosWithdrawServer(), asset.getCfosWithdrawUser(), asset.getCfosWithdrawPwd());
				
				BigDecimal balance = cfosRpc.getBitcoinBalance("");
				if (null == balance) {
					LOGGER.error("查询余额钱包返回为空。symbol <== " + asset.getSymbol());
					return;
				}
				
				if (balance.compareTo(withdrawData.getAmount()) < 0) {
					LOGGER.error("余额不足。symbol <== " + asset.getSymbol() + "\t balance <== " + balance);
					updateData.setMessage("余额不足，余额：" + balance);
					updateData.setProcessTime(System.currentTimeMillis());
					withdrawDataService.updateById(updateData);
					return;
				}
				
				SendAssetLog log = new SendAssetLog();
				log.setId(Utils.uuid());
				log.setSymbol(asset.getSymbol());
				log.setFromAddress(asset.getAddress());
				log.setToAddress(withdrawData.getAddress());
				log.setAmount(withdrawData.getAmount());
				log.setSendTime(new Date());
				
				String txid = null;
				
				try {
					BigDecimal amount = withdrawData.getAmount();
					if (null != asset.getTxFee() && BigDecimal.ZERO.compareTo(asset.getTxFee()) < 0) {
						amount = amount.subtract(asset.getTxFee());
					}
					if (BigDecimal.ZERO.compareTo(amount) >= 0) {
						updateData.setIsSend(false);
						updateData.setIsUpload(false);
						updateData.setIsProcessed(true);
						updateData.setMessage("提币金额不正确，金额 - 手续费 = " + amount);
						updateData.setProcessTime(System.currentTimeMillis());
						withdrawDataService.updateById(updateData);
						
						log.setSendNote("提币金额不正确，金额 - 手续费 = " + amount);
						sendAssetLogService.save(log);
						return;
					}
					if (0 == asset.getAssetType()) {
						txid = cfosRpc.sendBitcoin("", withdrawData.getAddress(), amount);
					} else {
						txid = cfosRpc.sendCfos(asset.getAddress(), withdrawData.getAddress(), amount, asset.getAbcAddress(), asset.getAddress(), asset.getAbcAddress());
					}
					updateData.setSendTimes((null == withdrawData.getSendTimes()) ? 1 : withdrawData.getSendTimes() + 1);
				} catch (ConnectException e) {
					updateData.setProcessTime(System.currentTimeMillis());
					withdrawDataService.updateById(updateData);
					
					log.setSendNote("钱包未能正常连接。 [自动任务]");
					sendAssetLogService.save(log);
					return;
				} catch (Exception e) {
					updateData.setIsSend(false);
					updateData.setIsUpload(true);
					updateData.setMessage("钱包返回异常，详情：" + e.getMessage());
					updateData.setProcessTime(System.currentTimeMillis());
					withdrawDataService.updateById(updateData);
					
					log.setSendNote("钱包返回异常，详情：" + e.getMessage());
					sendAssetLogService.save(log);
					return;
				}
				
				if (Utils.isEmpty(txid)) {
					LOGGER.error("钱包未返回交易ID。更新信息 serno <== " + withdrawData.getSerno());
					updateData.setMessage("钱包未返回交易ID");
					if (updateData.getSendTimes() >= 3) {
						updateData.setIsSend(false);
						updateData.setIsUpload(true);
						updateData.setMessage("尝试发送3次失败");
					}
					updateData.setProcessTime(System.currentTimeMillis());
					withdrawDataService.updateById(updateData);
					
					log.setSendNote("钱包未返回交易ID。 [自动任务]");
					sendAssetLogService.save(log);
					return;
				}
				
				updateData.setIsSend(false);
				updateData.setIsUpload(true);
				withdrawDataService.updateById(updateData);
				
				log.setTxid(txid);;
				log.setSendNote("成功[自动任务]");
				sendAssetLogService.save(log);
				
				doWithdrawdataresult(withdrawData, txid, null);
			} else if (withdrawData.getIsUpload()) {
				doWithdrawdataresult(withdrawData, withdrawData.getTxid(), withdrawData.getMessage());
			} else {
				WithdrawData updateData = new WithdrawData();
				updateData.setId(withdrawData.getId());
				updateData.setIsProcessed(true);
				updateData.setProcessTime(System.currentTimeMillis());
				withdrawDataService.updateById(updateData);
			}
		} catch (Exception e) {
			LOGGER.error("提币数据发送上传处理异常。", e);
		} finally {
			LOGGER.debug("提币数据发送上传处理结束......");
		}
	}

	/**
	 * 充币数据获取任务
	 */
	@Scheduled(initialDelay = 2000, fixedDelay = 10000)
	public void runGetChargeData() {
		LOGGER.error("quartz start get charge data process, instance <- " + this + ", thread <- " + Thread.currentThread());
		LOGGER.debug("钱包充币数据轮询开始......");
		reloadAsset();
		for (String server : ASSET_CLIENT.keySet()) {
			Asset asset = ASSET_CLIENT.get(server);
			try {
				LOGGER.debug("钱包充币数据轮询开始。 server <== " + server);
				
				CfosRpc rpc = CfosRpc.getRpc(asset.getCfosChargeServer(), asset.getCfosChargeUser(), asset.getCfosChargePwd());
				
				long blockHeight = (null == asset.getBlockHeight()) ? 1 : asset.getBlockHeight();
				Long blockCount = rpc.getBlockCount();

				if (blockCount >= blockHeight) {
					Block block = rpc.getBlock(blockHeight);

					List<String> txids = block.getTx();
					for (int i = 0; i < txids.size(); ++i) {
						try {
							LOGGER.error("block height <== " + blockHeight + "\t txid <== " + txids.get(i));
							com.wallet.platform.cfos.result.Transaction transaction = rpc.getTransaction(txids.get(i));
							if (null == transaction || Utils.isEmpty(transaction.getTxid())) {
								LOGGER.error("[not wallet transaction] <== " + txids.get(i));
								continue;
							}
							LOGGER.error("[wallet transaction] <== " + txids.get(i));
							saveTransaction(block, transaction, asset.getSymbol());
						} catch (Exception e) {
							LOGGER.error("保存交易信息异常，区块高度 <==" + blockHeight, e);
						}
					}

					// 更新区块高度
					Asset updateAsset = new Asset();
					updateAsset.setCfosChargeServer(asset.getCfosChargeServer());
					updateAsset.setCfosChargeUser(asset.getCfosChargeUser());
					updateAsset.setBlockHeight(blockHeight + 1);
					assetService.updateByCfos(updateAsset);

				} else {
					LOGGER.debug("暂无新区块...... symbol <== " + asset.getSymbol() + "\tblockcount <== " + blockCount);
				}

			} catch (Exception e) {
				LOGGER.error("钱包充币数据轮询异常。 symbol <== " + asset.getSymbol(), e);
			} finally {
				LOGGER.debug("钱包充币数据轮询结束。 symbol <== " + asset.getSymbol());
			}
		}
		LOGGER.debug("钱包充币数据轮询结束......");
		LOGGER.error("quartz end get charge data process, instance <- " + this + ", thread <- " + Thread.currentThread());
	}

	@Scheduled(initialDelay = 14000, fixedDelay = 5000)
	public void runUploadChargeData() {
		LOGGER.debug("上传充币数据轮询开始......");
		try {
			Transaction uploadData = transactionService.getFristUpload();
			if (null == uploadData) {
				LOGGER.debug("暂无需要上传的充币数据......");
				return;
			}

			Transaction sendData = transactionService.getSend(uploadData);
			if (null != sendData) {
				LOGGER.info("invalid tx <== " + uploadData.getTxid());
				transactionService.updateTxUploadProcessed(uploadData.getTxid(), uploadData.getAddress());
				return;
			}

			StringBuffer buf = new StringBuffer();
			buf.append("txid=").append(uploadData.getTxid());
			buf.append("&address=").append(uploadData.getAddress());
			buf.append("&amount=").append(String.format("%.8f", uploadData.getAmount()));

			OutputBean outputBean = PlatfromClient.getOutput("charge", buf.toString());

			Transaction updateData = new Transaction();
			updateData.setId(uploadData.getId());
			updateData.setIsUpload(true);
			if (null == outputBean || outputBean.getCode() == 1) {
				LOGGER.info("返回值解析为空，或服务端要求重新上传。txid <== " + uploadData.getTxid());
				updateData.setUploadMessage((null == outputBean) ? "返回值解析错误" : "code = 1, " + outputBean.getMessage());
			} else {
				updateData.setIsUpload(false);
				updateData.setIsConfirm(true);
				updateData.setUploadMessage(outputBean.getMessage());
				LOGGER.info("充币交易上传成功。txid <== " + uploadData.getTxid());
			}
			updateData.setProcessTime(System.currentTimeMillis());
			transactionService.updateById(updateData);
		} catch (Exception e) {
			LOGGER.error("上传充币数据轮询异常。", e);
		} finally {
			LOGGER.debug("上传充币数据轮询结束......");
		}
	}

	@Scheduled(initialDelay = 15000, fixedDelay = 20000)
	public void runUploadConfirmData() {
		LOGGER.debug("上传充币确认数据轮询开始......");
		try {
			Transaction confirmData = transactionService.getFristConfirm();
			if (null == confirmData) {
				LOGGER.debug("暂无需要上传的充币确认数据......");
				return;
			}
			
			Transaction updateData = new Transaction();
			updateData.setId(confirmData.getId());
			
			Asset asset = assetService.getBySymbol(confirmData.getSymbol());
			if (null == asset) {
				LOGGER.error("asset not found. symbol <== " + confirmData.getSymbol());
				return;
			}

			CfosRpc rpc = CfosRpc.getRpc(asset.getCfosChargeServer(), asset.getCfosChargeUser(), asset.getCfosChargePwd());
			
			com.wallet.platform.cfos.result.Transaction tran = rpc.getTransaction(confirmData.getTxid());
			if (null == tran) {
				LOGGER.error("cfos return null. txid <== " + confirmData.getTxid());
				updateData.setIsConfirm(true);
				updateData.setConfirmMessage("钱包返回空");
				updateData.setProcessTime(System.currentTimeMillis());
				transactionService.updateById(updateData);
				return;
			}

			if (null == tran.getConfirmations() || tran.getConfirmations() == 0) {
				LOGGER.debug("no confirmations. txid <== " + confirmData.getTxid());
				updateData.setIsConfirm(true);
				updateData.setConfirmMessage("暂未确认");
				updateData.setProcessTime(System.currentTimeMillis());
				transactionService.updateById(updateData);
				return;
			}

			StringBuffer buf = new StringBuffer();
			buf.append("txid=").append(confirmData.getTxid());
			buf.append("&confirms=").append(tran.getConfirmations());

			OutputBean outputBean = PlatfromClient.getOutput("confirm", buf.toString());

			if (null == outputBean || outputBean.getCode() == 1) {
				LOGGER.info("返回值解析为空，或服务端要求重新上传。txid <== " + confirmData.getTxid());
				updateData.setConfirmMessage((null == outputBean) ? "返回值解析错误" : "code = 1, " + outputBean.getMessage());
				updateData.setIsConfirm(true);
			} else {
				updateData.setIsConfirm(false);
				updateData.setConfirmMessage(String.valueOf(tran.getConfirmations()));
				updateData.setIsProcessed(true);
				updateData.setConfirmMessage(outputBean.getMessage());
				LOGGER.info("充币交易确认上传成功。txid <== " + confirmData.getTxid() + "\tconfirms <== " + tran.getConfirmations());
				
				try {
					if (null != asset.getIsChange() && asset.getIsChange() && !Utils.isEmpty(asset.getChangeAddress())) {
						if (0 == asset.getAssetType()) {
							BigDecimal balance = rpc.getBitcoinBalance("");
							if (null != balance && balance.compareTo(new BigDecimal(1.1)) >= 0) {
								LOGGER.error("自动转币。symbol <== " + asset.getSymbol() + "\t balance <== " + balance);
								String txid = rpc.sendBitcoin("", asset.getChangeAddress(), BigDecimal.ONE);
								if (null != txid) {
									LOGGER.debug("充币地址自动转币到冷钱包地址[" + confirmData.getAddress() + "] ==> [" + asset.getChangeAddress() + "]\t amount <== " + String.format("%.8f", confirmData.getAmount()) + "\t txid <== " + txid);
								}
							}
						} else if (1 == asset.getAssetType()) {
							//TODO CFOS 自动转币
							BigDecimal balance = rpc.getAddressBalance(updateData.getAddress());
							if (null != balance && balance.compareTo(BigDecimal.ZERO) > 0) {
								LOGGER.error("自动转币。symbol <== " + asset.getSymbol() + "\t balance <== " + balance);
								String txid = rpc.sendCfos(updateData.getAddress(), asset.getChangeAddress(), balance, asset.getChangeFeeAddress(), updateData.getAddress(), asset.getChangeFeeAddress());
								if (null != txid) {
									LOGGER.debug("充币地址自动转币到冷钱包地址[" + confirmData.getAddress() + "] ==> [" + asset.getChangeAddress() + "]\t amount <== " + String.format("%.8f", confirmData.getAmount()) + "\t txid <== " + txid);
								}
							}
						}
					}
				} catch (Exception e) {
					LOGGER.error("充币地址自动转币到冷钱包地址异常。", e);
				}
			}
			updateData.setProcessTime(System.currentTimeMillis());
			transactionService.updateById(updateData);
			
		} catch (Exception e) {
			LOGGER.error("上传充币确认数据轮询异常。", e);
		} finally {
			LOGGER.debug("上传充币确认数据轮询结束......");
		}
	}

	private void saveTransaction(Block block, com.wallet.platform.cfos.result.Transaction transaction, String symbol) {
		List<TransactionDetail> details = transaction.getDetails();
		List<Transaction> trans = new ArrayList<Transaction>();
		for (TransactionDetail detail : details) {
			if (Utils.isEmpty(detail.getSymbol())) {
				detail.setSymbol(symbol);
			}
			if (detail.getAmount().compareTo(BigDecimal.ZERO) != 0) {
				if (ALL_ASSET.containsKey(detail.getSymbol())) {
					Transaction tran = new Transaction();
					tran.setId(Utils.uuid());
					tran.setTxid(transaction.getTxid());
					tran.setAccount(detail.getAccount());
					tran.setAddress(detail.getAddress());
					tran.setAmount(detail.getAmount());
					tran.setBlockHeight(block.getHeight());
					tran.setCategory(detail.getCategory());
					tran.setConfirms(tran.getConfirms());
					tran.setIsConfirm(false);
					Boolean isUse = ALL_ASSET.get(detail.getSymbol()).getIsUse();
					if (null != isUse && isUse) {
						tran.setIsProcessed(false);
						tran.setIsUpload(true);
					} else {
						tran.setIsProcessed(true);
						tran.setIsUpload(false);
						tran.setUploadMessage("币种配置未开启");
					}
					tran.setProcessTime(System.currentTimeMillis());
					tran.setSymbol(detail.getSymbol());
					tran.setTimeReceived(transaction.getTime());
					trans.add(tran);
					LOGGER.error("[DB] txid <== " + tran.getTxid() + "\t symbol <== " + tran.getSymbol() + "\t address <== " + tran.getAddress() + "\t amount <== " + tran.getAmount());
				}
			}
		}
		if (trans.size() > 0) {
			transactionService.save(trans);
		}
	}

	/**
	 * 从钱包或者缓存中获取地址
	 * 
	 * @param asset
	 *            资产符号
	 * @param numbers
	 *            数量
	 * @return
	 * @throws Exception
	 */
	private String getAddresses(Asset asset, Integer numbers) throws Exception {
		String addresses = ADDR_MAP.get(asset.getSymbol());
		if (!Utils.isEmpty(addresses)) {
			return addresses;
		}
		if (numbers > 100) {
			numbers = 100;
		}
		Address address = null;
		CfosRpc rpc = CfosRpc.getRpc(asset.getCfosChargeServer(), asset.getCfosChargeUser(), asset.getCfosChargePwd());
		StringBuffer buf = new StringBuffer();
		if (Utils.isEmpty(asset.getAddress()) && Utils.isEmpty(asset.getAbcAddress())) {
			for (int i = 0; i < numbers; ++i) {
				address = rpc.getNewAddress("");
				if (null != address && !Utils.isEmpty(address.getAddress())) {
					buf.append(",").append(address.getAddress());
				}
			}
		} else {
			for (int i = 0; i < numbers; ++i) {
				address = rpc.getNewAddress(asset.getSymbol(), "");
				if (null != address && !Utils.isEmpty(address.getAddress())) {
					buf.append(",").append(address.getAddress());
				}
			}
		}
		addresses = buf.toString();
		if (addresses.length() > 1) {
			addresses = new String(addresses.substring(1));
		}
		return addresses;
	}

	private void doWithdrawdataresult(WithdrawData withdrawData, String txid, String error) {
		StringBuffer buf = new StringBuffer();
		buf.append("txid=").append(txid);
		buf.append("&success=").append(!Utils.isEmpty(txid));
		buf.append("&message=").append(error);
		buf.append("&symbol=").append(withdrawData.getSymbol());
		buf.append("&serno=").append(withdrawData.getSerno());
		buf.append("&address=").append(withdrawData.getAddress());
		buf.append("&amount=").append(String.format("%.8f", withdrawData.getAmount()));
		OutputBean output = null;
		try {
			output = PlatfromClient.getOutput("withdrawdataresult", buf.toString());
		} catch (Exception e) {
			LOGGER.error("上传提币数据异常。serno <== " + withdrawData.getSerno(), e);
		}
		WithdrawData updateData = new WithdrawData();
		updateData.setId(withdrawData.getId());
		updateData.setIsSend(false);
		updateData.setSendTimes(null);

		if (Utils.isEmpty(withdrawData.getTxid())) {
			updateData.setTxid(txid);
		}
		if (Utils.isEmpty(withdrawData.getMessage())) {
			updateData.setMessage(error);
		}

		if (null == output || output.getCode() == 1) {
			updateData.setIsUpload(true);
			if (null == output) {
				updateData.setIsSuccess(false);
				updateData.setUploadMessage("空返回值");
			} else {
				updateData.setIsSuccess(output.getSuccess());
				updateData.setUploadMessage("code = 1, " + output.getMessage());
			}
		} else {
			updateData.setIsUpload(false);
			updateData.setIsSuccess(output.getSuccess());
			updateData.setUploadMessage(output.getMessage());
			updateData.setIsProcessed(true);
			updateData.setMessage(error);
			withdrawDataService.updateById(updateData);
		}
		updateData.setProcessTime(System.currentTimeMillis());
		withdrawDataService.updateById(updateData);
	}

	private void reloadAsset() {
		try {
			List<Asset> assets = assetService.queryAllInuse();
			synchronized (ALL_ASSET) {
				ALL_ASSET.clear();
				ASSET_CLIENT.clear();
				for (Asset asset : assets) {
					ALL_ASSET.put(asset.getSymbol(), asset);
					if (!Utils.isEmpty(asset.getCfosChargeServer())) {
						if (!ASSET_CLIENT.containsKey(asset.getCfosChargeServer())) {
							ASSET_CLIENT.put(asset.getCfosChargeServer(), asset);
						}
					} else if (!ASSET_CLIENT.containsKey("default")) {
						ASSET_CLIENT.put("default", asset);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("加载所有资产信息异常。", e);
		}
	}

	@Resource
	private IAssetService assetService;

	@Resource
	private IWithdrawDataService withdrawDataService;

	@Resource
	private ITransactionService transactionService;
	
	@Resource
	private ISendAssetLogService sendAssetLogService;

	private static final Logger LOGGER = Logger.getLogger(DataPollingTask.class);

	private static final Map<String, String> ADDR_MAP = new HashMap<String, String>();

	private static final Map<String, Asset> ALL_ASSET = new HashMap<String, Asset>();
	
	private static final Map<String, Asset> ASSET_CLIENT = new HashMap<String, Asset>();

}
