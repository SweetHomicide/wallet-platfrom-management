package com.wallet.platform.cfos;

import java.math.BigDecimal;

import com.wallet.platform.cfos.exception.CfosInnerException;
import com.wallet.platform.cfos.exception.ConnectException;

interface ICfosRpcInterface {

	/**
	 * 获取区块总数
	 * 
	 * @return 当前的区块总数
	 * @throws ConnectException
	 * @throws CfosInnerException
	 */
	CfosRpcResult getBlockCount() throws ConnectException, CfosInnerException, Exception;

	/**
	 * 获取区块HASH
	 * 
	 * @param blockIndex
	 *            区块索引
	 * @return 区块HASH
	 * @throws ConnectException
	 * @throws CfosInnerException
	 */
	CfosRpcResult getBlockHash(Long blockIndex) throws ConnectException, CfosInnerException, Exception;

	/**
	 * 获取区块信息
	 * 
	 * @param blockHash
	 *            区块HASH
	 * @return 区块信息JSON
	 * @throws ConnectException
	 * @throws CfosInnerException
	 */
	CfosRpcResult getBlock(String blockHash) throws ConnectException, CfosInnerException, Exception;

	/**
	 * 获取交易信息
	 * 
	 * @param txid
	 *            交易ID
	 * @return 交易信息JSON
	 * @throws ConnectException
	 * @throws CfosInnerException
	 */
	CfosRpcResult getTransaction(String txid) throws ConnectException, CfosInnerException, Exception;

	/**
	 * 发送资产
	 * 
	 * @param fromAssetAddress
	 *            发送地址
	 * @param toAssetAddress
	 *            发送到的地址
	 * @param amount
	 *            发送金额
	 * @param cashFeeAddress
	 *            手续费地址
	 * @param assetChangeAddress
	 *            发送变更地址，为空则自动生成
	 * @param cashFeeChangeAddress
	 *            手续费变更地址，为空则自动生成
	 * @return
	 * @throws ConnectException
	 * @throws CfosInnerException
	 */
	CfosRpcResult sendAsset2Address(String fromAssetAddress, String toAssetAddress, BigDecimal amount, String cashFeeAddress, String assetChangeAddress, String cashFeeChangeAddress) throws ConnectException,
			CfosInnerException, Exception;
	
	/**
	 * 获取新地址
	 * 
	 * @param symbol
	 *            资产符号
	 * @param account
	 *            账户
	 * @return
	 * @throws ConnectException
	 * @throws CfosInnerException
	 */
	CfosRpcResult getNewAddress(String symbol, String account) throws ConnectException, CfosInnerException, Exception;

	CfosRpcResult getNewAddress(String account) throws ConnectException, CfosInnerException, Exception;
	
	/**
	 * 获取地址余额
	 * 
	 * @param adddress
	 *            地址
	 * @return
	 * @throws ConnectException
	 * @throws CfosInnerException
	 */
	CfosRpcResult getAddressBalance(String adddress) throws ConnectException, CfosInnerException, Exception;

	CfosRpcResult getBitcoinBalance(String account) throws ConnectException, CfosInnerException, Exception;

	CfosRpcResult sendBitcoin(String account, String address, BigDecimal amount) throws ConnectException, CfosInnerException, Exception;

}
