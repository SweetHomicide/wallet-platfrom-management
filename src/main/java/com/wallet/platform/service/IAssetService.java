package com.wallet.platform.service;

import java.util.List;

import com.wallet.platform.po.Asset;

public interface IAssetService extends ICommonService<Asset, String> {

	List<Asset> queryAllInuse();

	Asset getBySymbol(String symbol);

	void updateByCfos(Asset asset);

	List<Asset> queryAll(int offset, int limit);

	Integer getCount();

}
