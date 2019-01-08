package com.wallet.platform.cfos.result;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class SendAssetToAddress implements Serializable {

	private static final long serialVersionUID = 1L;

	@JSONField(name = "txid")
	private String txid;

	@JSONField(name = "assetchangeaddress")
	private String assetChangeAddress;

	@JSONField(name = "cashchangeaddress")
	private String cashChangeAddress;

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public String getAssetChangeAddress() {
		return assetChangeAddress;
	}

	public void setAssetChangeAddress(String assetChangeAddress) {
		this.assetChangeAddress = assetChangeAddress;
	}

	public String getCashChangeAddress() {
		return cashChangeAddress;
	}

	public void setCashChangeAddress(String cashChangeAddress) {
		this.cashChangeAddress = cashChangeAddress;
	}

}
