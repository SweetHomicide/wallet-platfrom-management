package com.wallet.platform.service;

public interface ICommonService<T, K> {

	void save(T entity);

	void updateById(T entity);

	void deleteById(K id);

	T getById(K id);

}
