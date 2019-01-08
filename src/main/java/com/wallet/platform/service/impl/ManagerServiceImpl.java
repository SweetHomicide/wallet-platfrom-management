package com.wallet.platform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wallet.platform.bean.ParamBean;
import com.wallet.platform.dao.IBaseDao;
import com.wallet.platform.dao.IManagerDao;
import com.wallet.platform.po.Manager;
import com.wallet.platform.service.IManagerService;
import com.wallet.platform.util.Utils;

@Service
public class ManagerServiceImpl extends CommonServiceImpl<Manager, String> implements IManagerService {

	@Resource
	private IManagerDao managerDao;
	
	@Override
	protected IBaseDao<Manager, String> getDao() {
		return managerDao;
	}

	@Override
	public List<Manager> queryAll(int offset, int limit) {
		return managerDao.queryByParam(Utils.getParamMap(
				new ParamBean("start", offset),
				new ParamBean("size", limit)
				));
	}
	
	@Override
	public Integer getCount() {
		return managerDao.getCountByParam(Utils.getParamMap());
	}

}
