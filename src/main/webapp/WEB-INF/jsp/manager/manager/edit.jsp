<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form id="manager-form" role="form" class="form-horizontal" action="" method="post">
	<input type="hidden" id="type" name="type" value="${type}">
	<div class="form-group">
		<label for="managerName" class="col-sm-2 control-label">用户名</label>
		<div class="col-sm-4">
			<input type="text" <c:if test="${'ADD' != type}">readonly="readonly"</c:if> class="form-control" id="managerName" name="managerName" value="${mg.managerName}" placeholder="请输入管理用户名" />
		</div>
		<label for="managerPwd" class="col-sm-2 control-label">用户密码</label>
		<div class="col-sm-4">
			<input type="password" class="form-control" id="managerPwd" name="managerPwd" placeholder="请输入管理用户密码" />
		</div>
	</div>
	<div class="form-group">
		<label for="inuse" class="col-sm-2 control-label">启用</label>
		<div class="col-sm-4">
			<input type="checkbox" value="true" id="inuse" name="inuse" <c:if test="${not empty mg.inuse and mg.inuse}"> checked="checked"</c:if> />是
		</div>
		<label for="realname" class="col-sm-2 control-label">姓名</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="realname" name="realname" value="${mg.realname}" placeholder="请输入姓名" />
		</div>
	</div>
	<div class="form-group">
		<label for="menus" class="col-sm-2 control-label">菜单</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" id="menus" name="menus" value="${mg.menus}" placeholder="请输入管理菜单" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">所有菜单</label>
		<div class="col-sm-10"></div>
		<div class="col-sm-offset-2 col-sm-10">┌─[101] 信息管理</div>
		<div class="col-sm-offset-2 col-sm-10">├───[101-101] 管理员信息</div>
		<div class="col-sm-offset-2 col-sm-10">├───[101-102] 资产信息</div>
		<div class="col-sm-offset-2 col-sm-10">├─[102] 钱包管理</div>
		<div class="col-sm-offset-2 col-sm-10">├───[102-101] 充币数据</div>
		<div class="col-sm-offset-2 col-sm-10">├───[102-102] 提币数据</div>
		<div class="col-sm-offset-2 col-sm-10">└───[102-103] 发送</div>
	</div>
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<span class="tips" id="message"></span>
		</div>
	</div>
</form>

<script type="text/javascript">
	function doSubmit(cfm) {
		$.post("${basePath}manager/manager/edit", $("#manager-form").serialize(), function(data) {
			data = eval("(" + data + ")");
			if (data["success"]) {
				$("#message").html(font("green", data["message"]));
				cfm.close();
				$("#manager-table").bootstrapTable("refresh");
			} else {
				$("#message").html(font("red", data["message"]));
			}
		});
	}
</script>
