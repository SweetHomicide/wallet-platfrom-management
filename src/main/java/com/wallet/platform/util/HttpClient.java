package com.wallet.platform.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class HttpClient {
	
	private static final Logger logger = Logger.getLogger(HttpClient.class);
	
	private static final String CHARSET = "UTF-8";
	
	public static final String TYPE_JSON = "application/json";
	
	public static final String TYPE_XFORM_URLENCODED = "application/x-www-form-urlencoded";
	
	public static final String METHOD_POST = "POST";
	
	public static final String METHOD_GET = "GET";
	
	private String message;
	
	public boolean sendRequest(String method, String data, String path, String type) {
		logger.debug("[url] <== " + path);
		logger.debug("[method] <== " + method);
		logger.debug("[type] <== " + type);
		logger.debug("[data] <== " + data);
		
		if (METHOD_GET.equalsIgnoreCase(method)) {
			path += path.indexOf("?") >= 0 ? "&" : "?";
			path += data;
			data = "";
		}
		HttpURLConnection conn = getConn(method, data, path, type);
		if (null == conn) {
			message = "无法建立到远程的连接";
			return false;
		}
		
		OutputStreamWriter out = null;
		BufferedReader in = null;
		try {
			if (!Utils.isEmpty(data) && METHOD_POST.equalsIgnoreCase(method)) {
				out = new OutputStreamWriter(conn.getOutputStream(), CHARSET);
				out.write(data);
				out.flush();
			}
			
			// 获取响应状态
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				message = "HTTP响应状态[" + conn.getResponseCode() + "]不是：" + HttpURLConnection.HTTP_OK;
				return false;
			}
			
			// 获取响应内容体
			String line, result = "";
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
			message = result;
			return true;
		} catch (IOException e) {
			message = "发送" + method + "请求异常";
			logger.error(message + " [path]=" + path, e);
			return false;
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (Exception e) {
				logger.error("关闭HTTP输出流异常，[path]=" + path, e);
			}
			try {
				if (null != in) {
					in.close();
				}
			} catch (Exception e) {
				logger.error("关闭HTTP输入流异常，[path]=" + path, e);
			}
			logger.debug("[out] ==> " + message);
		}
	}
	
	/**
	 * 下载远程文件
	 * @param url 远程文件路径
	 * @param file 本地存放的文件
	 */
	public boolean getRemoteFile(String method, String url, File file) {
		
		logger.debug("url = " + url + ", file = " + file.getAbsolutePath());
		// 发送POST请求
		HttpURLConnection conn = getConn(method, null, url, HttpClient.TYPE_XFORM_URLENCODED);
		if (null == conn) {
			message = "无法建立到远程的连接";
			return false;
		}
		
		OutputStream out = null;
		InputStream in = null;
		try {
			// 获取响应状态
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				message = "HTTP响应状态[" + conn.getResponseCode() + "]不是：" + HttpURLConnection.HTTP_OK;
				return false;
			}
			
			out = new FileOutputStream(file);
			
			// 获取响应主体
			in = conn.getInputStream();
			byte[] datas = new byte[1024];
			int len = -1;
			while ((len = in.read(datas)) != -1) {
				out.write(datas, 0, len);
				out.flush();
			}
			
			return true;
		} catch (IOException e) {
			message = "发送" + method + "请求异常";
			logger.error(message + " [url]=" + url, e);
			return false;
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (Exception e) {
				logger.error("关闭HTTP输出流异常，[url]=" + url, e);
			}
			try {
				if (null != in) {
					in.close();
				}
			} catch (Exception e) {
				logger.error("关闭HTTP输入流异常，[url]=" + url, e);
			}
		}			
	}
	
	public static HttpURLConnection getConn(String method, String data, String path, String type) {
		try {
			URL url = new URL(path);
			if (url.getProtocol().toLowerCase().equals("https")) {
				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(null, TRUST_ALL_CERTS, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (url.getProtocol().toLowerCase().equals("https")) {
				((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);
			}
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type", type);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setConnectTimeout(10 * 1000); // 10秒钟超时
			if (!Utils.isEmpty(data)) {
				conn.setRequestProperty("Content-Length", "" + data.length());
			}
			return conn;
		} catch (Exception e) {
			logger.error("建立HTTP连接异常，[url]=" + path, e);
		}
		return null;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static String encode(String data) {
		try {
			return URLEncoder.encode(URLEncoder.encode(data, "UTF-8"), "UTF-8");
		} catch (Exception e) {
			logger.error("URL编码异常", e);
			return "";
		}
	}
	
	public static String decode(String data) {
		try {
			return URLDecoder.decode(data, "UTF-8");
		} catch (Exception e) {
			logger.error("URL解码异常", e);
			return "";
		}
	}

	private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
	
	private static final TrustManager[] TRUST_ALL_CERTS = new TrustManager[] { new X509TrustManager() {

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return new java.security.cert.X509Certificate[] {};
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
			
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
		
		}
	} };
}
