<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form id="asset-form" role="form" class="form-horizontal" action="" method="post">
	<input type="hidden" id="type" name="type" value="${type}">
	<input type="hidden" id="id" name="id" value="${asset.id}">
	<div class="form-group">
		<label for="symbol" class="col-sm-2 control-label">资产符号</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="symbol" name="symbol" value="${asset.symbol}" placeholder="请输入资产符号" />
		</div>
		<label for="symbolName" class="col-sm-2 control-label">资产名称</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="symbolName" name="symbolName" value="${asset.symbolName}" placeholder="请输入资产名称" />
		</div>
	</div>
	<div class="form-group">
		<label for="isUse" class="col-sm-2 control-label">启用</label>
		<div class="col-sm-4">
			<input type="checkbox" value="true" id="isUse" name="isUse" <c:if test="${not empty asset.isUse and asset.isUse}"> checked="checked"</c:if> />是
		</div>
		<label for="blockHeight" class="col-sm-2 control-label">区块高度</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="blockHeight" name="blockHeight" value="${asset.blockHeight}" placeholder="请输入区块高度" />
		</div>
	</div>
	<div class="form-group">
		<label for="assetType" class="col-sm-2 control-label">接口类型</label>
		<div class="col-sm-10">
			<select id="assetType" name="assetType" class="form-control" onchange="setFeeAddressVisable(this.value);">
				<option value="0" <c:if test="${'0' == asset.assetType}"> selected="selected"</c:if> >Bitcoin</option>
				<option value="1" <c:if test="${'1' == asset.assetType}"> selected="selected"</c:if>>Cfos</option>
			</select>
		</div>
	</div>
	<hr/>
	<div class="form-group">
		<label for="cfosChargeServer" class="col-sm-2 control-label">充币服务器</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" id="cfosChargeServer" name="cfosChargeServer" value="${asset.cfosChargeServer}" placeholder="请输入充币服务器地址" />
		</div>
	</div>
	<div class="form-group">
		<label for="cfosChargeUser" class="col-sm-2 control-label">充币用户名</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="cfosChargeUser" name="cfosChargeUser" value="${asset.cfosChargeUser}" placeholder="请输入充币服务器用户名" />
		</div>
		<label for="cfosChargePwd" class="col-sm-2 control-label">充币密码</label>
		<div class="col-sm-4">
			<input type="password" class="form-control" id="cfosChargePwd" name="cfosChargePwd" value="${asset.cfosChargePwd}" placeholder="请输入充币服务器密码" />
		</div>
	</div>
	<div class="form-group">
		<label for="isChange" class="col-sm-2 control-label">自动转币</label>
		<div class="col-sm-4">
			<input type="checkbox" value="true" id="isChange" name="isChange" <c:if test="${not empty asset.isChange and asset.isChange}"> checked="checked"</c:if> />是
		</div>
		<label for="changeAddress" class="col-sm-2 control-label">转币地址</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="changeAddress" name="changeAddress" value="${asset.changeAddress}" placeholder="请输入自动转币地址" />
		</div>
	</div>
	<div class="form-group">
		<label for="changeFeeAddress" class="col-sm-2 control-label">手续地址</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" id="changeFeeAddress" name="changeFeeAddress" value="${asset.changeFeeAddress}" placeholder="请输入自动转币时手续费地址，接口类型为：Bitcoin时，本项目无效" />
		</div>
	</div>
	<hr/>
	<div class="form-group">
		<label for="cfosWithdrawServer" class="col-sm-2 control-label">提币服务器</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" id="cfosWithdrawServer" name="cfosWithdrawServer" value="${asset.cfosWithdrawServer}" placeholder="请输入提币服务器地址" />
		</div>
	</div>
	<div class="form-group">
		<label for="cfosWithdrawUser" class="col-sm-2 control-label">提币用户名</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="cfosWithdrawUser" name="cfosWithdrawUser" value="${asset.cfosWithdrawUser}" placeholder="请输入提币服务器用户名" />
		</div>
		<label for="cfosWithdrawPwd" class="col-sm-2 control-label">提币密码</label>
		<div class="col-sm-4">
			<input type="password" class="form-control" id="cfosWithdrawPwd" name="cfosWithdrawPwd" value="${asset.cfosWithdrawPwd}" placeholder="请输入提币服务器密码" />
		</div>
	</div>
	<div class="form-group">
		<label for="address" class="col-sm-2 control-label">发送地址</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="address" name="address" value="${asset.address}" placeholder="请输入发送地址" />
		</div>
		<label for="abcAddress" class="col-sm-2 control-label">手续费地址</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="abcAddress" name="abcAddress" value="${asset.abcAddress}" placeholder="请输入手续费地址" />
		</div>
	</div>
	<div class="form-group">
		<label for="txFee" class="col-sm-2 control-label">手续费</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" id="txFee" name="txFee" value="${asset.txFee}" placeholder="请输入手续费" />
		</div>
	</div>
	<hr/>
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<span class="tips" id="message"></span>
		</div>
	</div>
</form>

<script type="text/javascript">
	function doSubmit(cfm) {
		$.post("${basePath}manager/asset/edit", $("#asset-form").serialize(), function(data) {
			data = eval("(" + data + ")");
			if (data["success"]) {
				$("#message").html(font("green", data["message"]));
				cfm.close();
				$("#asset-table").bootstrapTable("refresh");
			} else {
				$("#message").html(font("red", data["message"]));
			}
		});
	}
	
	setFeeAddressVisable("${asset.assetType}");
	
	function setFeeAddressVisable(assetType) {
		if (0 == assetType || "0" == assetType) {
			$("#abcAddress").attr("readonly", "readonly");
			$("#changeFeeAddress").attr("readonly", "readonly");
		} else {
			$("#abcAddress").removeAttr("readonly");
			$("#changeFeeAddress").removeAttr("readonly");
		}
	}
</script>
