package com.wallet.platform.bean;

import java.io.Serializable;

public class ResultBean implements Serializable {

	private static final long serialVersionUID = 421345686965918012L;

	private Boolean success = false;

	private String message = null;

	private Object data;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
