package com.wallet.platform.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.wallet.platform.bean.ParamBean;

public final class Utils {
	
	public static boolean isEmpty(Object obj) {
		return null == obj || String.valueOf(obj).trim().length() == 0;
	}

	public static Map<String, Object> getParamMap(ParamBean ... paramBeans) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (null != paramBeans && paramBeans.length > 0) {
			for (int i = 0; i < paramBeans.length; ++i) {
				paramMap.put(paramBeans[i].getName(), paramBeans[i].getValue());
				if (paramBeans[i].isRequired()) {
					paramMap.put(paramBeans[i].getName() + "Required", true);
				}
			}
		}
		return paramMap;
	}

	public static String uuid() {
		return new String(UUID.randomUUID().toString().replace("-", ""));
	}
	
}
