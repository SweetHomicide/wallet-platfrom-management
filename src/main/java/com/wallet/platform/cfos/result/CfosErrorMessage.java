package com.wallet.platform.cfos.result;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CfosErrorMessage implements Serializable {

	private static final long serialVersionUID = 4959396662890200085L;

	@JSONField(name = "code")
	private Integer code; // 错误码

	@JSONField(name = "message")
	private String message; // 错误信息

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		return new StringBuffer("错误码：").append(code).append("，详情：").append(message).toString();
	}
}
