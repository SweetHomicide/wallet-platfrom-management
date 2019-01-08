package com.wallet.platform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.wallet.platform.bean.ParamBean;
import com.wallet.platform.dao.IBaseDao;
import com.wallet.platform.dao.IWithdrawDataDao;
import com.wallet.platform.po.WithdrawData;
import com.wallet.platform.service.IWithdrawDataService;
import com.wallet.platform.util.Utils;

@Service
public class WithdrawDataServiceImpl extends CommonServiceImpl<WithdrawData, String> implements IWithdrawDataService {

	private static final Logger logger = Logger.getLogger(WithdrawDataServiceImpl.class); 
	
	@Resource
	private IWithdrawDataDao dao;
	
	@Override
	protected IBaseDao<WithdrawData, String> getDao() {
		return dao;
	}

	@Override
	public void save(List<WithdrawData> datas) {
		if (null != datas && datas.size() > 0) {
			for (int i = 0; i < datas.size(); ++i) {
				WithdrawData data = datas.get(i);
				if (null == dao.getByParam(Utils.getParamMap(new ParamBean("serno", data.getSerno(), true)))) {
					dao.insert(data);
					logger.error("save data serno <== " + data.getSerno() + "\tsymbol <== " + data.getSymbol() + "\taddress <== " + data.getAddress() + "\tamount <== " + String.format("%.8f", data.getAmount()));
				} else {
					logger.error("exists data serno <== " + data.getSerno());
				}
			}
		}
	}

	@Override
	public WithdrawData getFirstOne() {
		WithdrawData data = dao.getFirstOne();
		if (null != data) {
			WithdrawData updateData = new WithdrawData();
			updateData.setId(data.getId());
			updateData.setIsSend(false);
			updateData.setProcessTime(System.currentTimeMillis());
			dao.updateById(updateData);
		}
		return data;
	}

	@Override
	public List<WithdrawData> queryLast(int offset, int limit) {
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
