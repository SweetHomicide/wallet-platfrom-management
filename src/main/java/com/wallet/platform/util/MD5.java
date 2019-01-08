package com.wallet.platform.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	/**
	 * MD5加密(信息加密算法)
	 * 
	 * @param message
	 *            要进行MD5加密的字符串
	 * @return 加密结果为32位字符串
	 */
	public static String getMD5(String message, boolean isUpperCase) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(message.getBytes("UTF-8"));
			String result = getHexString(messageDigest.digest());
			return isUpperCase ? result.toUpperCase() : result;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	/**
	 * java语言MD5加密
	 * 
	 * @param message
	 */
	public static String getMD5(String message) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(message.getBytes("UTF-8"));
			return getHexString(md5.digest());
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	private static final String getHexString(byte[] bytes) {
		StringBuffer md5StrBuff = new StringBuffer();
		String temp = "";
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(0xFF & bytes[i]);
			if (temp.length() == 1) {
				md5StrBuff.append("0");
			}
			md5StrBuff.append(temp);
        }
		return md5StrBuff.toString();
	}
}