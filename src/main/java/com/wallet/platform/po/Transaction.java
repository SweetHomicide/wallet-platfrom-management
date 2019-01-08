package com.wallet.platform.po;

import java.io.Serializable;
import java.math.BigDecimal;

public class Transaction implements Serializable {

	private static final long serialVersionUID = -3452392148858745581L;

	private String id; // ID

	private String txid; // 交易ID

	private String category; // 类型

	private String symbol; // 资产符号

	private String account; // 账户

	private String address; // 地址

	private Long blockHeight; // 区块高度

	private BigDecimal amount; // 金额

	private Integer confirms; // 确认数

	private Long timeReceived; // 接收时间戳

	private Boolean isUpload; // 是否上传

	private String uploadMessage; // 上传返回信息

	private Boolean isConfirm; // 是否确认

	private String confirmMessage; // 确认返回信息

	private Boolean isProcessed; // 是否已经处理

	private Long processTime; // 处理时间戳

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getConfirms() {
		return confirms;
	}

	public void setConfirms(Integer confirms) {
		this.confirms = confirms;
	}

	public Long getTimeReceived() {
		return timeReceived;
	}

	public void setTimeReceived(Long timeReceived) {
		this.timeReceived = timeReceived;
	}

	public Boolean getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(Boolean isUpload) {
		this.isUpload = isUpload;
	}

	public String getUploadMessage() {
		return uploadMessage;
	}

	public void setUploadMessage(String uploadMessage) {
		this.uploadMessage = uploadMessage;
	}

	public Boolean getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(Boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	public String getConfirmMessage() {
		return confirmMessage;
	}

	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}

	public Boolean getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public Long getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Long processTime) {
		this.processTime = processTime;
	}

	public Long getBlockHeight() {
		return blockHeight;
	}

	public void setBlockHeight(Long blockHeight) {
		this.blockHeight = blockHeight;
	}

}
