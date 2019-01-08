package com.wallet.platform.po;

import java.io.Serializable;
import java.math.BigDecimal;

public class WithdrawData implements Serializable {

	private static final long serialVersionUID = 3584276508917513357L;

	private String id; // ID

	private String symbol; // 资产符号

	private BigDecimal amount; // 金额

	private String address; // 地址

	private String serno; // 业务流水号

	private Boolean isSuccess; // 是否成功

	private String txid; // 交易ID

	private String message; // 信息

	private Boolean isSend; // 是否发送

	private Boolean isUpload; // 是否上传

	private String uploadMessage; // 上传返回信息

	private Boolean isProcessed; // 是否处理完成

	private Long processTime; // 处理时间戳

	private Integer sendTimes = 0; // 发送的次数

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSerno() {
		return serno;
	}

	public void setSerno(String serno) {
		this.serno = serno;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getIsSend() {
		return isSend;
	}

	public void setIsSend(Boolean isSend) {
		this.isSend = isSend;
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

	public Integer getSendTimes() {
		return sendTimes;
	}

	public void setSendTimes(Integer sendTimes) {
		this.sendTimes = sendTimes;
	}

}
