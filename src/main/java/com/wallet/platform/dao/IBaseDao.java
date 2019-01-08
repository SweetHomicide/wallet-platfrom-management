package com.wallet.platform.dao;

import java.util.List;
import java.util.Map;

public interface IBaseDao<T, K> {

	/**
	 * 插入方法
	 * 
	 * @param po
	 */
	void insert(T po);

	/**
	 * 更新方法，根据主键
	 * 
	 * @param po
	 */
	void updateById(T po);

	/**
	 * 删除方法，根据主键
	 * 
	 * @param id
	 */
	void deleteById(K id);

	/**
	 * 查询方法，根据主键
	 * 
	 * @param id
	 * @return
	 */
	T getById(K id);

	/**
	 * 查询方法，自定义属性
	 * 
	 * @param param
	 * @return
	 */
	T getByParam(Map<String, Object> param);

	/**
	 * 查询方法
	 * 
	 * @param param
	 * @return
	 */
	List<T> queryByParam(Map<String, Object> param);
}
