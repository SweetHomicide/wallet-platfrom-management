package com.wallet.platform.cfos.result;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public final class CfosResultType {
	
	private static final Map<String, Type> MAP = new HashMap<String, Type>();
	static {
		initAllType();
	}
	
	@SuppressWarnings("serial")
	private static void initAllType() {
		MAP.put(Long.class.getName(), new CfosResult<Long>(){}.getClass().getGenericSuperclass());
		MAP.put(BigDecimal.class.getName(), new CfosResult<BigDecimal>(){}.getClass().getGenericSuperclass());
		MAP.put(String.class.getName(), new CfosResult<String>(){}.getClass().getGenericSuperclass());
		MAP.put(Block.class.getName(), new CfosResult<Block>(){}.getClass().getGenericSuperclass());
		MAP.put(Transaction.class.getName(), new CfosResult<Transaction>(){}.getClass().getGenericSuperclass());
		MAP.put(SendAssetToAddress.class.getName(), new CfosResult<SendAssetToAddress>(){}.getClass().getGenericSuperclass());
	}
	
	private static Type getType(Class<?> clazz) throws Exception {
		if (!MAP.containsKey(clazz.getName())) {
			throw new Exception("[not rpc result class] -> " + clazz.getName());
		}
		return MAP.get(clazz.getName());
	}
	
	public static <T> CfosResult<T> fromJson(String json, Class<T> clazz) throws Exception {
		return JSON.parseObject(json, getType(clazz));
	}
}
