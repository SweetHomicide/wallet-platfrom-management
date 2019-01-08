<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">提币交易数据</div>
			<div class="panel-body">
				<table id="transaction-table" class="table" data-url="${basePath}manager/withdraw/datas" data-toggle="table" data-show-refresh="true" data-show-toggle="false" data-show-columns="true" data-search="true" data-select-item-name="toolbar1" data-pagination="true" data-side-pagination="server" data-sort-name="name" data-sort-order="desc">
					<thead>
						<tr>
							<th data-field="symbol">资产符号</th>
							<th data-field="address">地址</th>
							<th data-field="amount" data-formatter="formatAmount">金额</th>
							<th data-field="serno">业务流水</th>
							<th data-field="txid">交易ID</th>
							<th data-field="isSend" data-formatter="formatBoolean">发送</th>
							<th data-field="message" data-formatter="formatWrap">发送信息</th>
							<th data-field="isUpload" data-formatter="formatBoolean">上传</th>
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
	
	function formatWrap(value) {
		return "<span style=\"white-space:nowrap;\">" + $.trim(value) + "</span>";
	}
	
	function font(color, value) {
		return "<font color=\"" + color + "\">" + value + "</font>";
	}
</script>
