package com.wallet.platform.dao;

import java.util.Map;

import com.wallet.platform.po.Manager;

public interface IManagerDao extends IBaseDao<Manager, String> {

	Integer getCountByParam(Map<String, Object> params);

}
