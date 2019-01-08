package com.wallet.platform.cfos.exception;


public class CfosInnerException extends Exception {

	private static final long serialVersionUID = 6095070735119771418L;
	
	public CfosInnerException() {
		super("CFOS内部错误。");
	}
	
	public CfosInnerException(String message) {
		super(message);
	}
	
	public CfosInnerException(String message, Throwable cause) {
		super(message, cause);
	}
}
