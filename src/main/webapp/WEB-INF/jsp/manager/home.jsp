<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML>
<html>
<head>
<title>${systemName}</title>
<link type="text/css" rel="stylesheet" href="${basePath}resources/css/bootstrap.min.css">
<link type="text/css" rel="stylesheet" href="${basePath}resources/css/bootstrap-table.css">
<link type="text/css" rel="stylesheet" href="${basePath}resources/css/jquery-confirm.min.css">
<link type="text/css" rel="stylesheet" href="${basePath}resources/manager/css/manager.css">
<!--[if lt IE 9]>
<script type="text/javascript" src="${basePath}resources/js/html5shiv.js"></script>
<script type="text/javascript" src="${basePath}resources/js/respond.min.js"></script>
<![endif]-->
<script type="text/javascript">
	var basePath = "${basePath}";
</script>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#"> ${systemName} </a>
				<ul class="user-menu">
					<li class="dropdown pull-right">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							<span class="glyphicon glyphicon-user"></span> ${manager.realname} <span class="caret"></span>
						</a>
						<ul class="dropdown-menu" role="menu">
							<li>
								<a href="${basePath}manager/logout">
									<span class="glyphicon glyphicon-user"></span> 退出
								</a>
							</li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</nav>

	<div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">
		<ul id="manager-menu" class="nav menu">
			
		</ul>
	</div>
	
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">	
		<div class="row">
			<div class="col-sm-12">
				<h4 id="page-header" class="page-header"></h4>
			</div>
		</div>
		<div id="main-page">
		
		</div>
	</div>
</body>
<script type="text/javascript" src="${basePath}resources/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${basePath}resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${basePath}resources/js/jquery-confirm.min.js"></script>
<script type="text/javascript" src="${basePath}resources/manager/js/home.js"></script>
<script type="text/javascript">
	!function($) {
		$(document).on("click", "ul.nav li.parent > a > span.icon", function() {
			$(this).find('em:first').toggleClass("glyphicon-minus");
		});
		$(".sidebar span.icon").find('em:first').addClass("glyphicon-plus");
	}(window.jQuery);

	$(window).on('resize', function() {
		if ($(window).width() > 768) {
			$('#sidebar-collapse').collapse('show');
		} else {
			$('#sidebar-collapse').collapse('hide');
		}
	});
</script>
</html>
