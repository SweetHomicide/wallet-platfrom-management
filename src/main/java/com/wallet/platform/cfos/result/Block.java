package com.wallet.platform.cfos.result;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Block implements Serializable {

	private static final long serialVersionUID = -5339781082958516561L;

	@JSONField(name = "hash")
	private String hash;

	@JSONField(name = "confirmations")
	private Long confirmations;

	@JSONField(name = "size")
	private Long size;

	@JSONField(name = "height")
	private Long height;

	@JSONField(name = "version")
	private Long version;

	@JSONField(name = "merkleroot")
	private String merkleroot;

	@JSONField(name = "tx")
	private List<String> tx;

	@JSONField(name = "time")
	private Long time;

	@JSONField(name = "nonce")
	private Long nonce;

	@JSONField(name = "bits")
	private String bits;

	@JSONField(name = "difficulty")
	private Double difficulty;

	@JSONField(name = "chainwork")
	private String chainwork;

	@JSONField(name = "previousblockhash")
	private String previousblockhash;

	@JSONField(name = "nextblockhash")
	private String nextblockhash;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Long getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(Long confirmations) {
		this.confirmations = confirmations;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getMerkleroot() {
		return merkleroot;
	}

	public void setMerkleroot(String merkleroot) {
		this.merkleroot = merkleroot;
	}

	public List<String> getTx() {
		return tx;
	}

	public void setTx(List<String> tx) {
		this.tx = tx;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Long getNonce() {
		return nonce;
	}

	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

	public String getBits() {
		return bits;
	}

	public void setBits(String bits) {
		this.bits = bits;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public String getChainwork() {
		return chainwork;
	}

	public void setChainwork(String chainwork) {
		this.chainwork = chainwork;
	}

	public String getPreviousblockhash() {
		return previousblockhash;
	}

	public void setPreviousblockhash(String previousblockhash) {
		this.previousblockhash = previousblockhash;
	}

	public String getNextblockhash() {
		return nextblockhash;
	}

	public void setNextblockhash(String nextblockhash) {
		this.nextblockhash = nextblockhash;
	}

}
