package com.wallet.platform.service;

import java.util.List;

import com.wallet.platform.po.Manager;

public interface IManagerService extends ICommonService<Manager, String> {

	List<Manager> queryAll(int offset, int limit);

	Integer getCount();

}
