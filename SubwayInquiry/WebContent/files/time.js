$(document).ready(
		function() {

			
			// 点击地铁站
			$("ellipse").click(
					function() {
						
						redirection(this.id);
						
					});
			 
			
			
			// 点击换乘站
			$(".transpoint").click(
					function() {
						
						redirection(this.id);
						
					});
			
		});

function redirection(sid){
	document.title = "正在为您查询...";
	location.href = "time.jsp?sid="+sid;
}

function getStationTimeTable(sid){
	
	var url = "TimeTableAction";
	var param = {
			sid : sid
	};
	
	document.title = "正在为您查询...";
	jQuery.ajax({
		url : url,
		type : "post",
		dataType : "text",
		data : param,
		success : function(data) {
			document.title = "微铁 | 地铁时刻查询";
			alert(data);
		},
		error : function(data) {
			document.title = "微铁 | 地铁时刻查询";
			alert('数据加载失败，请重试');
		},
	});
}



