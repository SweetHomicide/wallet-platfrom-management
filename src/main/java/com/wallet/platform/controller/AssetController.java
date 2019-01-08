package com.wallet.platform.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wallet.platform.consts.Const;
import com.wallet.platform.po.Asset;
import com.wallet.platform.service.IAssetService;
import com.wallet.platform.util.Utils;

@Controller
@RequestMapping(value = "/manager/asset", produces = Const.PRODUCES)
public class AssetController extends BaseController {
	
	@Resource
	private IAssetService assetService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String asset() {
		return managerPath("asset/asset");
	}
	
	@RequestMapping(value="/load", method = RequestMethod.GET)
	public String load(Model model, String id, String type) {
		add(model, "type", type);
		if ("EDIT".equals(type) || "DEL".equals(type)) {
			add(model, "asset", assetService.getById(id));
		}
		return managerPath("asset/edit");
	}
	
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(String type, Asset asset) {
		if ("EDIT".equals(type)) {
			if (Utils.isEmpty(asset.getId())) {
				return toJson(false, "empty asset id", null);
			}
			if (null == asset.getIsChange()) {
				asset.setIsChange(false);
			}
			if (null == asset.getIsUse()) {
				asset.setIsUse(false);
			}
			assetService.updateById(asset);
			return toJson(true, "修改成功", null);
		} else if ("DEL".equals(type)) {
			if (Utils.isEmpty(asset.getId())) {
				return toJson(false, "empty asset id", null);
			}
			assetService.deleteById(asset.getId());
			return toJson(true, "删除成功", null);
		} else if ("ADD".equals(type)) {
			asset.setId(Utils.uuid());
			assetService.save(asset);
			return toJson(true, "新增成功", null);
		} else {
			return toJson(false, "unknown type <== " + type, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/datas", method = RequestMethod.GET)
	public String datas(HttpServletRequest request, Integer limit, Integer offset) {
		List<Asset> assets = assetService.queryAll((null == offset ? 0 : offset), (null == limit ? 10 : limit));
		Integer total = assetService.getCount();
		JSONObject json = new JSONObject();
		json.put("total", (null == total ? 0 : total));
		json.put("rows", assets);
		return JSON.toJSONString(json);
	}
	
}
