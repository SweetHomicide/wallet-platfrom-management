package com.wallet.platform.service.impl;

import com.wallet.platform.dao.IBaseDao;
import com.wallet.platform.service.ICommonService;

public abstract class CommonServiceImpl<T, K> implements ICommonService<T, K> {

	protected abstract IBaseDao<T, K> getDao();

	@Override
	public void save(T entity) {
		getDao().insert(entity);
	}

	@Override
	public void updateById(T entity) {
		getDao().updateById(entity);
	}

	@Override
	public void deleteById(K id) {
		getDao().deleteById(id);
	}

	@Override
	public T getById(K id) {
		return getDao().getById(id);
	}

}
