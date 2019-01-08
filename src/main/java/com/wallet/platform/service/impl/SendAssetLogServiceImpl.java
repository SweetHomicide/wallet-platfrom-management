package com.wallet.platform.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wallet.platform.dao.IBaseDao;
import com.wallet.platform.dao.ISendAssetLogDao;
import com.wallet.platform.po.SendAssetLog;
import com.wallet.platform.service.ISendAssetLogService;

@Service
public class SendAssetLogServiceImpl extends CommonServiceImpl<SendAssetLog, String> implements ISendAssetLogService {

	@Resource
	private ISendAssetLogDao dao;

	@Override
	protected IBaseDao<SendAssetLog, String> getDao() {
		return dao;
	}

}
