package com.wallet.platform.po;

import java.io.Serializable;

public class Setting implements Serializable {

	private static final long serialVersionUID = 2453768951302577252L;

	private String settingKey;

	private String settingValue;

	private String settingDesc;

	public String getSettingKey() {
		return settingKey;
	}

	public void setSettingKey(String settingKey) {
		this.settingKey = settingKey;
	}

	public String getSettingValue() {
		return settingValue;
	}

	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}

	public String getSettingDesc() {
		return settingDesc;
	}

	public void setSettingDesc(String settingDesc) {
		this.settingDesc = settingDesc;
	}

}
