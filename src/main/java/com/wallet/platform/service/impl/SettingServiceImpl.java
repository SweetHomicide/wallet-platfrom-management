package com.wallet.platform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wallet.platform.dao.IBaseDao;
import com.wallet.platform.dao.ISettingDao;
import com.wallet.platform.po.Setting;
import com.wallet.platform.service.ISettingService;
import com.wallet.platform.util.Utils;

@Service
public class SettingServiceImpl extends CommonServiceImpl<Setting, String> implements ISettingService {

	@Resource
	private ISettingDao dao;
	
	@Override
	protected IBaseDao<Setting, String> getDao() {
		return dao;
	}

	@Override
	public List<Setting> queryAll() {
		return dao.queryByParam(Utils.getParamMap());
	}

}
