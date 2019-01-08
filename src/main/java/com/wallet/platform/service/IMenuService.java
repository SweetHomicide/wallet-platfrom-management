package com.wallet.platform.service;

import java.util.List;

import com.wallet.platform.po.Menu;

public interface IMenuService extends ICommonService<Menu, String> {

	List<Menu> queryByIds(String menuIds);

}
