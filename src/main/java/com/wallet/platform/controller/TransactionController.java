package com.wallet.platform.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wallet.platform.consts.Const;
import com.wallet.platform.po.Transaction;
import com.wallet.platform.service.ITransactionService;

@Controller
@RequestMapping(value = "/manager/transaction", produces = Const.PRODUCES)
public class TransactionController extends BaseController {
	
	@Resource
	private ITransactionService transactionService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String transaction() {
		return managerPath("transaction/transaction");
	}
	
	@ResponseBody
	@RequestMapping(value = "/datas", method = RequestMethod.GET)
	public String datas(HttpServletRequest request, Integer limit, Integer offset) {
		List<Transaction> transactions = transactionService.queryLast((null == offset ? 0 : offset), (null == limit ? 10 : limit));
		Integer total = transactionService.getCount();
		JSONObject json = new JSONObject();
		json.put("total", (null == total ? 0 : total));
		json.put("rows", transactions);
		return JSON.toJSONString(json);
	}
	
}
