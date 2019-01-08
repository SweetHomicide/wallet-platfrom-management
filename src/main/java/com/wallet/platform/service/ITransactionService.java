package com.wallet.platform.service;

import java.util.List;

import com.wallet.platform.po.Transaction;

public interface ITransactionService extends ICommonService<Transaction, String> {

	void save(List<Transaction> trans);

	Transaction getFristUpload();

	Transaction getSend(Transaction transaction);

	void updateTxUploadProcessed(String txid, String address);

	Transaction getFristConfirm();

	List<Transaction> queryLast(int from, int size);

	Integer getCount();
}
