<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML>
<html>
<head>
<title>登录</title>
<link type="text/css" rel="stylesheet" href="${basePath}resources/css/bootstrap.min.css">
<link type="text/css" rel="stylesheet" href="${basePath}resources/manager/css/login.css">
</head>

<body>
	<div class="login">
		<form action="${basePath}manager/login" method="post">
			<table>
				<tr>
					<td colspan="2">
						<h3 class="text-center">登&emsp;录</h3>
					</td>
				</tr>
				<tr>
					<td>用户名</td>
					<td>
						<input type="text" required="required" class="form-control" id="username" name="username">
					</td>
				</tr>
				<tr>
					<td>密&emsp;码</td>
					<td>
						<input type="password" required="required" class="form-control" id="password" name="password">
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<button type="submit" class="btn btn-primary">登录</button>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<div class="message">${message}</div>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
