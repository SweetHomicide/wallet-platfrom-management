package com.wallet.platform.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wallet.platform.cfos.CfosRpc;
import com.wallet.platform.consts.Const;
import com.wallet.platform.po.Asset;
import com.wallet.platform.po.SendAssetLog;
import com.wallet.platform.po.WithdrawData;
import com.wallet.platform.service.IAssetService;
import com.wallet.platform.service.ISendAssetLogService;
import com.wallet.platform.service.IWithdrawDataService;
import com.wallet.platform.util.Utils;

@Controller
@RequestMapping(value = "/manager/withdraw", produces = Const.PRODUCES)
public class WithdrawController extends BaseController {
	
	@Resource
	private IWithdrawDataService withdrawDataService;
	
	@Resource
	private ISendAssetLogService sendAssetLogService;
	
	@Resource
	private IAssetService assetService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String WithdrawData() {
		return managerPath("withdraw/withdraw");
	}
	
	@ResponseBody
	@RequestMapping(value = "/datas", method = RequestMethod.GET)
	public String datas(HttpServletRequest request, Integer limit, Integer offset) {
		List<WithdrawData> datas = withdrawDataService.queryLast((null == offset ? 0 : offset), (null == limit ? 10 : limit));
		Integer total = withdrawDataService.getCount();
		JSONObject json = new JSONObject();
		json.put("total", (null == total ? 0 : total));
		json.put("rows", datas);
		return JSON.toJSONString(json);
	}
	
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public String send(Model model) {
		add(model, "assets", assetService.queryAllInuse());
		return managerPath("withdraw/send");
	}
	
	@ResponseBody
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public String send(HttpServletRequest request, String symbol, String address, BigDecimal amount) {
		
		SendAssetLog log = new SendAssetLog();
		log.setId(Utils.uuid());
		log.setSymbol(symbol);
		log.setToAddress(address);
		log.setAmount(amount);
		log.setSendTime(new Date());
		
		Asset asset = assetService.getBySymbol(symbol);
		if (null == asset) {
			log.setSendNote("资产信息不存在。 用户名：" + loginManager(request).getManagerName());
			sendAssetLogService.save(log);
			return toJson(false, "资产信息不存在。", null);
		}
		
		log.setFromAddress(asset.getAddress());
		
		try {
			CfosRpc cfosRpc = CfosRpc.getRpc(asset.getCfosWithdrawServer(), asset.getCfosWithdrawUser(), asset.getCfosWithdrawPwd());
			
			if (null == cfosRpc) {
				log.setSendNote("获取钱包接口失败。 用户名：" + loginManager(request).getManagerName());
				sendAssetLogService.save(log);
				return toJson(false, "获取钱包接口失败。", null);
			}
			
			BigDecimal balance = cfosRpc.getBitcoinBalance("");
			if (null == balance) {
				log.setSendNote("查询钱包余额返回为空。 用户名：" + loginManager(request).getManagerName());
				sendAssetLogService.save(log);
				return toJson(false, "查询钱包余额返回为空。", null);
			}
			
			if (balance.compareTo(amount) < 0) {
				log.setSendNote("钱包余额不足。余额：" + String.format("%.8f", balance) + " 用户名：" + loginManager(request).getManagerName());
				sendAssetLogService.save(log);
				return toJson(false, "钱包余额不足。余额：" + String.format("%.8f", balance), null);
			}
			
			String txid = null;
			if (0 == asset.getAssetType()) {
				txid = cfosRpc.sendBitcoin("", address, amount);
			} else if (1 == asset.getAssetType()) {
				txid = cfosRpc.sendCfos(asset.getAddress(), address, amount, asset.getAbcAddress(), asset.getAddress(), asset.getAbcAddress());
			}
			if (Utils.isEmpty(txid)) {
				log.setSendNote("发送钱包返回为空。 用户名：" + loginManager(request).getManagerName());
				sendAssetLogService.save(log);
				return toJson(false, "发送钱包返回为空。", null);
			}
			
			log.setTxid(txid);
			log.setSendNote("发送成功。 用户名：" + loginManager(request).getManagerName());
			sendAssetLogService.save(log);
			return toJson(true, "发送成功。交易ID：" + txid, null);
		} catch (Exception e) {
			log.setSendNote("发送失败。详情：" + e.getMessage() + " 用户名：" + loginManager(request).getManagerName());
			sendAssetLogService.save(log);
			return toJson(true, "发送失败。详情：" + e.getMessage(), null);
		}
		
	}
	
}
