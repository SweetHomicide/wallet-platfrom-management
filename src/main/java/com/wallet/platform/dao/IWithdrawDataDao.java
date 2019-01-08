package com.wallet.platform.dao;

import java.util.Map;

import com.wallet.platform.po.WithdrawData;

public interface IWithdrawDataDao extends IBaseDao<WithdrawData, String> {

	WithdrawData getFirstOne();

	Integer getCountByParam(Map<String, Object> params);

}
