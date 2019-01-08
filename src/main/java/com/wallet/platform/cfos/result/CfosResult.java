package com.wallet.platform.cfos.result;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CfosResult<T> implements Serializable {

	private static final long serialVersionUID = -8681326705234282681L;

	@JSONField(name = "id")
	private Integer id; // ID

	@JSONField(name = "error")
	private CfosErrorMessage error; // 错误信息

	@JSONField(name = "result")
	private T result; // 结果

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CfosErrorMessage getError() {
		return error;
	}

	public void setError(CfosErrorMessage error) {
		this.error = error;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
