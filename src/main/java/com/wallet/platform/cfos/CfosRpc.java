package com.wallet.platform.cfos;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.wallet.platform.cfos.exception.CfosInnerException;
import com.wallet.platform.cfos.exception.ConnectException;
import com.wallet.platform.cfos.result.Address;
import com.wallet.platform.cfos.result.Block;
import com.wallet.platform.cfos.result.CfosResult;
import com.wallet.platform.cfos.result.CfosResultType;
import com.wallet.platform.cfos.result.SendAssetToAddress;
import com.wallet.platform.cfos.result.Transaction;

public final class CfosRpc {

	private ICfosRpcInterface rpcInterface;
	
	private static final Map<String, CfosRpc> CACHE_MAP = new HashMap<String, CfosRpc>();
	
	public static CfosRpc getRpc(String server, String user, String password) throws ConnectException, Exception {
		String key = new String(server + ":" + user + ":" + password);
		if (!CACHE_MAP.containsKey(key)) {
			CACHE_MAP.put(key, new CfosRpc(server, user, password));
		}
		return CACHE_MAP.get(key);
	}

	private CfosRpc(String server, String user, String password) throws ConnectException, Exception {
		rpcInterface = new CfosRpcAdapter(server, user, password);
	}

	public Long getBlockCount() throws ConnectException, CfosInnerException, Exception {
		CfosRpcResult rpcResult = rpcInterface.getBlockCount();
		CfosResult<Long> result = CfosResultType.fromJson(rpcResult.getResult(), Long.class);
		if (rpcResult.isSuccess()) {
			return (null == result.getResult()) ? 0 : result.getResult();
		}
		return throwError(rpcResult, result);
	}

	public String getBlockHash(Long blockIndex) throws ConnectException, CfosInnerException, Exception {
		CfosRpcResult rpcResult = rpcInterface.getBlockHash(blockIndex);
		CfosResult<String> result = CfosResultType.fromJson(rpcResult.getResult(), String.class);
		if (rpcResult.isSuccess()) {
			return (null == result.getResult()) ? "" : result.getResult();
		}
		return throwError(rpcResult, result);
	}

	public Block getBlock(String blockHash) throws ConnectException, CfosInnerException, Exception {
		CfosRpcResult rpcResult = rpcInterface.getBlock(blockHash);
		CfosResult<Block> result = CfosResultType.fromJson(rpcResult.getResult(), Block.class);
		if (rpcResult.isSuccess()) {
			return result.getResult();
		}
		return throwError(rpcResult, result);
	}

	public Block getBlock(Long blockIndex) throws ConnectException, CfosInnerException, Exception {
		return getBlock(getBlockHash(blockIndex));
	}

	public Transaction getTransaction(String txid) throws ConnectException, CfosInnerException, Exception {
		CfosRpcResult rpcResult = rpcInterface.getTransaction(txid);
		CfosResult<Transaction> result = CfosResultType.fromJson(rpcResult.getResult(), Transaction.class);
		if (rpcResult.isSuccess()) {
			return result.getResult();
		}
		return throwError(rpcResult, result);
	}

	public Address getNewAddress(String symbol, String account) throws ConnectException, CfosInnerException, Exception {
		CfosRpcResult rpcResult = rpcInterface.getNewAddress(symbol, account);
		CfosResult<String> result = CfosResultType.fromJson(rpcResult.getResult(), String.class);
		if (rpcResult.isSuccess()) {
			Address address = new Address();
			address.setAddress(symbol);
			address.setAccount(account);
			address.setAddress((null == result.getResult()) ? "" : result.getResult());
			return address;
		}
		return throwError(rpcResult, result);
	}
	
	public Address getNewAddress(String account) throws ConnectException, CfosInnerException, Exception {
		CfosRpcResult rpcResult = rpcInterface.getNewAddress(account);
		CfosResult<String> result = CfosResultType.fromJson(rpcResult.getResult(), String.class);
		if (rpcResult.isSuccess()) {
			Address address = new Address();
			address.setAccount(account);
			address.setAddress((null == result.getResult()) ? "" : result.getResult());
			return address;
		}
		return throwError(rpcResult, result);
	}

	private <T> T throwError(CfosRpcResult rpcResult, CfosResult<?> result) throws ConnectException, CfosInnerException {
		if (401 == rpcResult.getCode()) {
			throw new ConnectException("用户名密码授权失败，请参考配置项[rpcuser]和[rpcpassword]进行配置。");
		}
		throw new CfosInnerException(result.getError().toString());
	}
	
	public BigDecimal getAddressBalance(String adddress) throws ConnectException, CfosInnerException, Exception {
		CfosRpcResult rpcResult = rpcInterface.getAddressBalance(adddress);
		CfosResult<BigDecimal> result = CfosResultType.fromJson(rpcResult.getResult(), BigDecimal.class);
		if (rpcResult.isSuccess()) {
			return (null == result.getResult()) ? BigDecimal.ZERO : result.getResult();
		}
		return throwError(rpcResult, result);
	}
	
	public BigDecimal getBitcoinBalance(String account) throws ConnectException, CfosInnerException, Exception {
		CfosRpcResult rpcResult = rpcInterface.getBitcoinBalance(account);
		CfosResult<BigDecimal> result = CfosResultType.fromJson(rpcResult.getResult(), BigDecimal.class);
		if (rpcResult.isSuccess()) {
			return (null == result.getResult()) ? BigDecimal.ZERO : result.getResult();
		}
		return throwError(rpcResult, result);
	}
	
	public String sendBitcoin(String account, String address, BigDecimal amount) throws ConnectException, CfosInnerException, Exception {
		CfosRpcResult rpcResult = rpcInterface.sendBitcoin(account, address, amount);
		CfosResult<String> result = CfosResultType.fromJson(rpcResult.getResult(), String.class);
		if (rpcResult.isSuccess()) {
			return (null == result.getResult()) ? "" : result.getResult();
		}
		return throwError(rpcResult, result);
	}
	
	public String sendCfos(String fromAssetAddress, String toAssetAddress, BigDecimal amount,
			String cashFeeAddress, String assetChangeAddress, String cashFeeChangeAddress) throws ConnectException, CfosInnerException, Exception {
		CfosRpcResult rpcResult = rpcInterface.sendAsset2Address(fromAssetAddress, toAssetAddress, amount, cashFeeAddress, assetChangeAddress, cashFeeChangeAddress);
		CfosResult<SendAssetToAddress> result = CfosResultType.fromJson(rpcResult.getResult(), SendAssetToAddress.class);
		if (rpcResult.isSuccess()) {
			return (null == result.getResult()) ? "" : result.getResult().getTxid();
		}
		return throwError(rpcResult, result);
	}
	
	// getBitcoinBalance();  sendBitcoin(String fromAddress, String toAddress, BigDecimal amount);
}
