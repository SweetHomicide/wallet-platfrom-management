package com.wallet.platform.po;

import java.io.Serializable;
import java.math.BigDecimal;

public class Asset implements Serializable {

	private static final long serialVersionUID = -514820153200468997L;

	private String id; // ID

	private String symbol; // 资产符号

	private String symbolName; // 资产名称

	private Boolean isUse; // 是否使用

	private String address; // 资产发送地址

	private String abcAddress; // 手续费地址

	private Long blockHeight; // 区块高度

	private BigDecimal txFee; // 手续费

	private Integer assetType; // 资产类型 0-比特币; 1-CFOS

	private String cfosChargeServer; // 充币服务器地址

	private String cfosChargeUser; // 充币服务器用户名

	private String cfosChargePwd; // 充币服务器密码

	private String cfosWithdrawServer; // 提币服务器地址

	private String cfosWithdrawUser; // 提币服务器用户名

	private String cfosWithdrawPwd; // 提币服务器密码

	private Boolean isChange; // 是否转移

	private String changeAddress; // 转移地址

	private String changeFeeAddress; // 转移时手续费地址

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

	public String getSymbolName() {
		return symbolName;
	}

	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}

	public Boolean getIsUse() {
		return isUse;
	}

	public void setIsUse(Boolean isUse) {
		this.isUse = isUse;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAbcAddress() {
		return abcAddress;
	}

	public void setAbcAddress(String abcAddress) {
		this.abcAddress = abcAddress;
	}

	public Long getBlockHeight() {
		return blockHeight;
	}

	public void setBlockHeight(Long blockHeight) {
		this.blockHeight = blockHeight;
	}

	public String getCfosChargeServer() {
		return cfosChargeServer;
	}

	public void setCfosChargeServer(String cfosChargeServer) {
		this.cfosChargeServer = cfosChargeServer;
	}

	public String getCfosChargeUser() {
		return cfosChargeUser;
	}

	public void setCfosChargeUser(String cfosChargeUser) {
		this.cfosChargeUser = cfosChargeUser;
	}

	public String getCfosChargePwd() {
		return cfosChargePwd;
	}

	public void setCfosChargePwd(String cfosChargePwd) {
		this.cfosChargePwd = cfosChargePwd;
	}

	public String getCfosWithdrawServer() {
		return cfosWithdrawServer;
	}

	public void setCfosWithdrawServer(String cfosWithdrawServer) {
		this.cfosWithdrawServer = cfosWithdrawServer;
	}

	public String getCfosWithdrawUser() {
		return cfosWithdrawUser;
	}

	public void setCfosWithdrawUser(String cfosWithdrawUser) {
		this.cfosWithdrawUser = cfosWithdrawUser;
	}

	public String getCfosWithdrawPwd() {
		return cfosWithdrawPwd;
	}

	public void setCfosWithdrawPwd(String cfosWithdrawPwd) {
		this.cfosWithdrawPwd = cfosWithdrawPwd;
	}

	public Boolean getIsChange() {
		return isChange;
	}

	public void setIsChange(Boolean isChange) {
		this.isChange = isChange;
	}

	public String getChangeAddress() {
		return changeAddress;
	}

	public void setChangeAddress(String changeAddress) {
		this.changeAddress = changeAddress;
	}

	public BigDecimal getTxFee() {
		return txFee;
	}

	public void setTxFee(BigDecimal txFee) {
		this.txFee = txFee;
	}

	public Integer getAssetType() {
		return assetType;
	}

	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
	}

	public String getChangeFeeAddress() {
		return changeFeeAddress;
	}

	public void setChangeFeeAddress(String changeFeeAddress) {
		this.changeFeeAddress = changeFeeAddress;
	}

}
