package com.wallet.platform.dao;

import java.util.List;
import java.util.Map;

import com.wallet.platform.po.Menu;

public interface IMenuDao extends IBaseDao<Menu, String> {

	List<Menu> queryByIds(Map<String, Object> params);

}
