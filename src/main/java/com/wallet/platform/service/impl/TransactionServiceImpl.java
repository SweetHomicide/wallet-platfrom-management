package com.wallet.platform.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.wallet.platform.bean.ParamBean;
import com.wallet.platform.dao.IBaseDao;
import com.wallet.platform.dao.ITransactionDao;
import com.wallet.platform.po.Transaction;
import com.wallet.platform.service.ITransactionService;
import com.wallet.platform.util.Utils;

@Service
public class TransactionServiceImpl extends CommonServiceImpl<Transaction, String> implements ITransactionService {

	private static final Logger LOGGER = Logger.getLogger(TransactionServiceImpl.class);
			
	@Resource
	private ITransactionDao dao;
	
	@Override
	protected IBaseDao<Transaction, String> getDao() {
		return dao;
	}

	@Override
	public void save(List<Transaction> trans) {
		if (null != trans && trans.size() > 0) {
			Transaction oldTransaction = null;
			for (Transaction tran : trans) {
				oldTransaction = dao.getByParam(Utils.getParamMap(
						new ParamBean("txid", tran.getTxid(), true),
						new ParamBean("category", tran.getCategory(), true),
						new ParamBean("address", tran.getAddress(), true),
						new ParamBean("amount", tran.getAmount(), true)
						));
				if (null == oldTransaction) {
					LOGGER.info("save tx: txid <== " + tran.getTxid() + "\tcategory <== " + tran.getCategory() + "\taddress <== " + tran.getAddress() + "\tamount <== " + String.format("%.8f", tran.getAmount()));
					dao.insert(tran);
				} else {
					LOGGER.error("exists tx: txid <== " + tran.getTxid() + "\tcategory <== " + tran.getCategory() + "\taddress <== " + tran.getAddress() + "\tamount <== " + String.format("%.8f", tran.getAmount()));
				}
			}
		}
		
	}

	@Override
	public Transaction getFristUpload() {
		Transaction t = dao.getFristUpload();
		if (null != t) {
			Transaction update = new Transaction();
			update.setId(t.getId());
			update.setIsUpload(false);
			update.setProcessTime(System.currentTimeMillis());
			dao.updateById(update);
		}
		return t;
	}

	@Override
	public Transaction getSend(Transaction transaction) {
		return dao.getByParam(Utils.getParamMap(
				new ParamBean("txid", transaction.getTxid(), true),
				new ParamBean("category", "send", true),
				new ParamBean("address", transaction.getAddress(), true),
				new ParamBean("amount", BigDecimal.ZERO.subtract(transaction.getAmount()), true)
				));
	}

	@Override
	public void updateTxUploadProcessed(String txid, String address) {
		dao.updateTxUploadProcessed(Utils.getParamMap(
				new ParamBean("txid", txid), 
				new ParamBean("address", address),
				new ParamBean("processTime", System.currentTimeMillis())
				));
	}

	@Override
	public Transaction getFristConfirm() {
		Transaction t = dao.getFristConfirm();
		if (null != t) {
			Transaction update = new Transaction();
			update.setId(t.getId());
			update.setIsConfirm(false);
			update.setProcessTime(System.currentTimeMillis());
		}
		return t;
	}

	@Override
	public List<Transaction> queryLast(int from, int size) {
		return dao.queryByParam(Utils.getParamMap(
				new ParamBean("start", from),
				new ParamBean("size", size)
				));
	}

	@Override
	public Integer getCount() {
		return dao.getCountByParam(Utils.getParamMap());
	}

}
