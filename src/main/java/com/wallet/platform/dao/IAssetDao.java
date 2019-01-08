package com.wallet.platform.dao;

import java.util.Map;

import com.wallet.platform.po.Asset;

public interface IAssetDao extends IBaseDao<Asset, String> {

	void updateByCfos(Asset asset);

	Integer getCountByParam(Map<String, Object> params);

}
