<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">充币交易数据</div>
			<div class="panel-body">
				<table id="transaction-table" class="table" data-url="${basePath}manager/transaction/datas" data-toggle="table" data-show-refresh="true" data-show-toggle="false" data-show-columns="true" data-search="true" data-select-item-name="toolbar1" data-pagination="true" data-side-pagination="server" data-sort-name="name" data-sort-order="desc">
					<thead>
						<tr>
							<th data-field="txid">交易ID</th>
							<th data-field="category" data-formatter="formatCategory">类型</th>
							<th data-field="symbol">资产符号</th>
							<th data-field="address">地址</th>
							<th data-field="amount" data-formatter="formatAmount">金额</th>
							<th data-field="timeReceived" data-formatter="formatDate1">接收时间</th>
							<th data-field="isUpload" data-formatter="formatBoolean">上传</th>
							<th data-field="isConfirm" data-formatter="formatBoolean">确认</th>
							<th data-field="isProcessed" data-formatter="formatBoolean">已处理</th>
							<th data-field="processTime" data-formatter="dateFmt">处理时间</th>
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
	
	function font(color, value) {
		return "<font color=\"" + color + "\">" + value + "</font>";
	}
</script>
