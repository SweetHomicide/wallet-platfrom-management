$(function() {
	loadMenu();
});

function loadMenu() {
	$.get(basePath + "manager/menus?t=" + new Date().getTime(), function(data) {
		try {
			data = eval("(" + data + ")");
			if (data["success"]) {
				var menus = data["data"];
				var html = "";
				for (var i = 0; i < menus.length; ++i) {
					html += "<li role=\"presentation\" class=\"divider\"></li>";
					html += "<li class=\"parent\">";
					html += "	<a href=\"#\">";
					html += "		<span class=\"glyphicon glyphicon-list\"></span> " + menus[i]["menuName"] + " <span data-toggle=\"collapse\" href=\"#sub-item-" + i + "\" class=\"icon pull-right\"><em class=\"glyphicon glyphicon-s glyphicon-plus\"></em></span>";
					html += "	</a>";
					var subMenus = menus[i]["subMenus"];
					if (subMenus && subMenus.length > 0) {
						html += "<ul class=\"children collapse\" id=\"sub-item-" + i + "\">";
						for (var j = 0; j < subMenus.length; ++j) {
							html += "<li>";
							html += "	<a href=\"javascript:void(0);\" onclick=\"loadPage('" + basePath + subMenus[j]["menuHref"] + "', '" + menus[i]["menuName"] + " / " + subMenus[j]["menuName"] + "');\">";
							html += "		<span class=\"glyphicon\"></span> " + subMenus[j]["menuName"];
							html += "	</a>";
							html += "</li>";
						}
						html += "</ul>";
					}
					html += "</li>";
				}
				$("#manager-menu").html(html);
			}
		} catch (e) {
			
		}
	});
}

function loadPage(url, header) {
	$("#page-header").html(header);
	$("#main-page").load(url);
}

function dateFmt(date) {
	if (isNaN(date)) {
		return "";
	}
	date = new Date(date);
	var year = date.getFullYear();
	var month = date.getMonth();
	month = month < 9 ? "0" + (month + 1) : (month + 1);
	var day = date.getDate();
	day = day < 10 ? "0" + day : day;
	
	var hour = date.getHours();
	hour = hour < 10 ? "0" + hour : hour;
	var min = date.getMinutes();
	min = min < 10 ? "0" + min : min;
	var sec = date.getSeconds();
	sec = sec < 10 ? "0" + sec : sec;
	
	return year + "-" + month + "-" + day + "&nbsp;" + hour + ":" + min + ":" + sec;
}