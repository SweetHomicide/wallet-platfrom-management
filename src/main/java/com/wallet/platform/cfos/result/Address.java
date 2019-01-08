package com.wallet.platform.cfos.result;

import java.io.Serializable;

public class Address implements Serializable {

	private static final long serialVersionUID = 7556083536026529471L;

	private String symbol;

	private String account;

	private String address;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String toString() {
		return new StringBuffer("资产：").append(symbol).append("，账户：").append(account).append("，地址：").append(address).toString();
	}
}
