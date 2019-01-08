<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">管理员数据</div>
			<div class="panel-body">
				<table id="manager-table" class="table" data-url="${basePath}manager/manager/datas" data-toggle="table" data-show-refresh="true" data-show-toggle="false" data-show-columns="true" data-search="true" data-select-item-name="toolbar1" data-pagination="true" data-side-pagination="server" data-sort-name="name" data-sort-order="desc">
					<thead>
						<tr>
							<th data-field="managerName" data-formatter="formatOper">操作&emsp;<button id="btn-edit" onclick="showForm('ADD', '');" class="btn btn-sm btn-success">新增</button></th>
							<th data-field="managerName">用户名</th>
							<th data-field="realname">姓名</th>
							<th data-field="inuse" data-formatter="formatBoolean">启用</th>
							<th data-field="errorTimes">登录错误次数</th>
							<th data-field="lastLoginTime" data-formatter="dateFmt">最近登录时间</th>
							<th data-field="lastLogoutTime" data-formatter="dateFmt">最近退出时间</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="${basePath}resources/js/bootstrap-table.js"></script>
<script type="text/javascript">
	function formatCategory(value) {
		return "send" == $.trim(value) ? font("red", "发送") : font("green", "接收");
	}
	
	function formatAmount(value) {
		var amount = new Number(value).toFixed(8);
		return value < 0 ? font("red", amount) : font("green", amount);
	}
	
	function formatDate1(value) {
		return dateFmt(1000 * value);
	}
	
	function formatBoolean(value) {
		return value ? font("green", "是") : font("red", "否");
	}
	
	function formatOper(value) {
		return "<div style=\"width:110px;\"><button id=\"btn-edit\" onclick=\"showForm('EDIT', '" + value + "');\" class=\"btn btn-sm btn-info\">编辑</button>&emsp;<button id=\"btn-del\" onclick=\"showForm('DEL', '" + value + "');\" class=\"btn btn-sm btn-danger\">删除</button></div>";
	}
	
	function showForm(type, id) {
		var cfm = $.confirm({
			content : "url:${basePath}manager/manager/load?id=" + id + "&type=" + type,
			title : ("EDIT" == type ? "编辑" : "DEL" == type ? "删除" : "ADD" == type ? "新增" : "") + "资产信息",
			columnClass: "large",
			closeIcon: true,
			buttons: {
				cancel: {
        			text: "取消",
            		btnClass: "btn-danger",
            		action: function() {
            			
            		}
        		},
        		ok: {
        			text: "确定",
            		btnClass: "btn-primary",
            		action: function() {
            			doSubmit(cfm);
            			return false;
            		}
        		}        		
        	}
		});
	}
	
	function font(color, value) {
		return "<font color=\"" + color + "\">" + value + "</font>";
	}
</script>
