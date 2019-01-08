package com.wallet.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wallet.platform.bean.ParamBean;
import com.wallet.platform.dao.IBaseDao;
import com.wallet.platform.dao.IMenuDao;
import com.wallet.platform.po.Menu;
import com.wallet.platform.service.IMenuService;
import com.wallet.platform.util.Utils;

@Service
public class MenuServiceImpl extends CommonServiceImpl<Menu, String> implements IMenuService {

	@Resource
	private IMenuDao menuDao;

	@Override
	protected IBaseDao<Menu, String> getDao() {
		return menuDao;
	}

	@Override
	public List<Menu> queryByIds(String menuIds) {
		if (Utils.isEmpty(menuIds)) {
			return new ArrayList<Menu>();
		}
		return menuDao.queryByIds(Utils.getParamMap(new ParamBean("menuIds", menuIds.split(","))));
	}

}
