package com.wallet.platform.service;

import java.util.List;

import com.wallet.platform.po.WithdrawData;

public interface IWithdrawDataService extends ICommonService<WithdrawData, String> {

	void save(List<WithdrawData> datas);

	WithdrawData getFirstOne();

	List<WithdrawData> queryLast(int offset, int limit);

	Integer getCount();

}
