package com.wallet.platform.cfos.result;

import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

public class TransactionDetail implements Serializable {

	private static final long serialVersionUID = 4848813620483984392L;

	@JSONField(name = "account")
	private String account;

	@JSONField(name = "symbol")
	private String symbol;

	@JSONField(name = "address")
	private String address;

	@JSONField(name = "category")
	private String category;

	@JSONField(name = "amount")
	private BigDecimal amount;

	@JSONField(name = "fee")
	private BigDecimal fee;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

}
