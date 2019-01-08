<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">提币发送</div>
			<div class="panel-body">
				<form id="send-form" role="form" class="form-horizontal" action="" method="post">
					<div class="form-group">
						<label for="symbol" class="col-sm-2 control-label">资产符号</label>
						<div class="col-sm-6">
							<select id="symbol" name="symbol" class="form-control">
								<c:forEach var="asset" items="${assets}">
									<option value="${asset.symbol}">${asset.symbolName}(${asset.symbol})</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-sm-4"></div>
					</div>
					<div class="form-group">
						<label for="address" class="col-sm-2 control-label">发送地址</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="address" name="address" placeholder="请输入发送地址" />
						</div>
						<div class="col-sm-4"></div>
					</div>
					<div class="form-group">
						<label for="amount" class="col-sm-2 control-label">发送金额</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="amount" name="amount" placeholder="请输入发送金额" />
						</div>
						<div class="col-sm-4"></div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="button" onclick="send();" class="btn btn-primary" style="width: 100px;">发送</button>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<span class="tips" id="message"></span>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	function send() {
		var symbol = $.trim($("#symbol").val());
		var address = $.trim($("#address").val());
		var amount = $.trim($("#amount").val());
		if ("" == symbol || "" == address || "" == amount || isNaN(amount)) {
			$.alert({
				title : "错误",
				content : font("red", "资产符号、发送地址和发送金额不能为空"),
				buttons: {
        			ok: {
        				text: "确定",
            			btnClass: "btn-danger",
            			action: function() {
            				
            			}
        			}
        		}
			});
			return;
		}
		
		$.confirm({
			title : "确认操作",
			content : font("red", "确定向 " + address + " 发送 " + amount + " 个 " + symbol + " 吗？"),
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
            			doSend();	
            		}
        		}        		
        	}
		});
	}
	
	function doSend() {
		$.post("${basePath}manager/withdraw/send", $("#send-form").serialize(), function(data) {
			data = eval("(" + data + ")");
			if (data["success"]) {
				$("#address").val("");
				$("#amount").val("");
				$("#message").html(font("green", data["message"]));
			} else {
				$("#message").html(font("red", data["message"]));
			}
		});
	}

	function font(color, value) {
		return "<font color=\"" + color + "\">" + value + "</font>";
	}
</script>
