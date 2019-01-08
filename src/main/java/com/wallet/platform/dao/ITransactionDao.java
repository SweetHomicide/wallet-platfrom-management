package com.wallet.platform.dao;

import java.util.Map;

import com.wallet.platform.po.Transaction;

public interface ITransactionDao extends IBaseDao<Transaction, String> {

	Transaction getFristUpload();

	void updateTxUploadProcessed(Map<String, Object> paramMap);

	Transaction getFristConfirm();

	Integer getCountByParam(Map<String, Object> paramMap);

}
