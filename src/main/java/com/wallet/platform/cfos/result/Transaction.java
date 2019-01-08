package com.wallet.platform.cfos.result;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Transaction implements Serializable {

	private static final long serialVersionUID = -3436883844043214543L;

	@JSONField(name = "amount")
	private BigDecimal amount;

	@JSONField(name = "fee")
	private BigDecimal fee;

	@JSONField(name = "confirmations")
	private Long confirmations;

	@JSONField(name = "blockhash")
	private String blockhash;

	@JSONField(name = "blockindex")
	private Long blockindex;

	@JSONField(name = "blocktime")
	private Long blocktime;

	@JSONField(name = "txid")
	private String txid;

	@JSONField(name = "time")
	private Long time;

	@JSONField(name = "timereceived")
	private Long timereceived;

	@JSONField(name = "details")
	private List<TransactionDetail> details;

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

	public Long getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(Long confirmations) {
		this.confirmations = confirmations;
	}

	public String getBlockhash() {
		return blockhash;
	}

	public void setBlockhash(String blockhash) {
		this.blockhash = blockhash;
	}

	public Long getBlockindex() {
		return blockindex;
	}

	public void setBlockindex(Long blockindex) {
		this.blockindex = blockindex;
	}

	public Long getBlocktime() {
		return blocktime;
	}

	public void setBlocktime(Long blocktime) {
		this.blocktime = blocktime;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Long getTimereceived() {
		return timereceived;
	}

	public void setTimereceived(Long timereceived) {
		this.timereceived = timereceived;
	}

	public List<TransactionDetail> getDetails() {
		return details;
	}

	public void setDetails(List<TransactionDetail> details) {
		this.details = details;
	}

}
