package com.wallet.platform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wallet.platform.bean.ParamBean;
import com.wallet.platform.dao.IAssetDao;
import com.wallet.platform.dao.IBaseDao;
import com.wallet.platform.po.Asset;
import com.wallet.platform.service.IAssetService;
import com.wallet.platform.util.Utils;

@Service
public class AssetServiceImpl extends CommonServiceImpl<Asset, String> implements IAssetService {

	@Resource
	private IAssetDao dao;

	@Override
	protected IBaseDao<Asset, String> getDao() {
		return dao;
	}

	@Override
	public List<Asset> queryAllInuse() {
		return dao.queryByParam(Utils.getParamMap(new ParamBean("inuse", true, true)));
	}

	@Override
	public Asset getBySymbol(String symbol) {
		return dao.getByParam(Utils.getParamMap(new ParamBean("inuse", true, true), new ParamBean("symbol", symbol, true)));
	}

	@Override
	public void updateByCfos(Asset asset) {
		dao.updateByCfos(asset);
	}

	@Override
	public List<Asset> queryAll(int offset, int limit) {
		return dao.queryByParam(Utils.getParamMap(
				new ParamBean("start", offset),
				new ParamBean("size", limit)
				));
	}
	
	@Override
	public Integer getCount() {
		return dao.getCountByParam(Utils.getParamMap());
	}

}
