package com.wallet.platform.cfos.exception;


public class ConnectException extends Exception {

	private static final long serialVersionUID = -5698556583801786407L;
	
	public ConnectException() {
		super("无法连接到远程服务器。");
	}
	
	public ConnectException(String message) {
		super(message);
	}
	
	public ConnectException(String message, Throwable cause) {
		super(message, cause);
	}
}
