package com.wallet.platform.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SendAssetLog implements Serializable {

	private static final long serialVersionUID = -8699266314602086856L;

	private String id; // ID

	private String symbol; // 资产符号

	private String fromAddress; // 发送地址

	private String toAddress; // 发送到的

	private BigDecimal amount; // 发送金额

	private String txid; // 交易ID

	private Date sendTime; // 发送时间

	private String sendNote; // 发送备注

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendNote() {
		return sendNote;
	}

	public void setSendNote(String sendNote) {
		this.sendNote = sendNote;
	}

}
