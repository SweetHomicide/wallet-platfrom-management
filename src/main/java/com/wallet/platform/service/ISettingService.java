package com.wallet.platform.service;

import java.util.List;

import com.wallet.platform.po.Setting;

public interface ISettingService extends ICommonService<Setting, String> {

	List<Setting> queryAll();

}
