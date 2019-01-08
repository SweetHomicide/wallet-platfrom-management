package com.wallet.platform.cfos;

class CfosRpcResult {

	private boolean success;

	private int code;

	private String result;

	public CfosRpcResult(int code, String result) {
		this.success = (200 == code);
		this.code = code;
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("success:").append(success);
		buf.append("\t").append("code:").append(code);
		buf.append("\t").append("result:").append(result);
		return buf.toString();
	}
}
