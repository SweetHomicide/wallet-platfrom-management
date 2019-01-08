package com.wallet.platform.util;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.wallet.platform.config.Config;
import com.wallet.platfrom.sdk.beans.OutputBean;
import com.wallet.platfrom.util.RSAResult;
import com.wallet.platfrom.util.RSAUtil;

public final class PlatfromClient {

	private static final Logger logger = Logger.getLogger(PlatfromClient.class);

	public static synchronized OutputBean getOutput(String method, String data) throws Exception {

		String publicKeyFile = Config.get("keyfile.public", null);
		if (Utils.isEmpty(publicKeyFile)) {
			logger.error("config[keyfile.public] not found.");
			return null;
		}
		
		String privateKeyFile = Config.get("keyfile.private", null);
		if (Utils.isEmpty(privateKeyFile)) {
			logger.error("config[keyfile.private] not found.");
			return null;
		}
		
		String platfromPath = Config.get("path.platfrom", null);
		if (Utils.isEmpty(platfromPath)) {
			logger.error("config[path.platfrom] not found.");
			return null;
		}

		RSAResult result = RSAUtil.encript(privateKeyFile, data, null);
		if (null == result) {
			logger.error("数据加密返回空，data <== " + data);
			return null;
		}
		StringBuffer buf = new StringBuffer();
		buf.append("wallet.platfrom.method=").append(method);
		buf.append("&wallet.platfrom.data=").append(result.getData());
		buf.append("&sign=").append(result.getSign());
		HttpClient client = new HttpClient();
		boolean success = client.sendRequest(HttpClient.METHOD_POST, buf.toString(), platfromPath, HttpClient.TYPE_XFORM_URLENCODED);
		if (success) {
			OutputBean output = JSON.parseObject(client.getMessage(), OutputBean.class);
			if (null == output) {
				logger.error("return data error. can not convert to OutputBean.");
				return null;
			}
			if (null == output.getSuccess() || !output.getSuccess()) {
				logger.error("返回结果解析success为空或失败，success < == " + output.getSuccess());
				return null;
			}
			if (Utils.isEmpty(output.getData()) || Utils.isEmpty(output.getSign())) {
				logger.error("返回结果解析data或sign为空，data <== " + output.getData());
				logger.error("sign <== " + output.getSign());
				logger.error("message <== " + output.getMessage());
				return null;
			}
			result = RSAUtil.decript(publicKeyFile, output.getData(), output.getSign(), null);
			if (null == result) {
				logger.error("数据解密失败");
				return null;
			}
			if (!result.isSignRight()) {
				logger.error("数据解密失败，签名错误");
				return null;
			}
			output = JSON.parseObject(result.getData(), OutputBean.class);
			if (null == output) {
				logger.error("data can not convert to OutputBean. data <== " + result.getData());
				return null;
			}
			if (null == output.getSuccess()) {
				logger.error("data解析success为空");
				return null;
			}
			return output;
		} else {
			logger.error("通讯失败，详情：" + client.getMessage());
		}
		return null;
	}

}
