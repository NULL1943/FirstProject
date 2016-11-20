var selectStartStation =false;	// 已选择起始站点
var selectEndStation =false;	// 已选择终止站点

var startStationSid = "";		// 起始站点id
var endStationSid = "";			// 终止站点id

$(document).ready(
		function() {

			
			// 点击地铁站
			$("ellipse").click(
					function() {
						
						selectStation(this.id);
						
					});
			 
			
			
			// 点击换乘站
			$(".transpoint").click(
					function() {
						
						selectStation(this.id);

					});
			
		});

function selectStation(sid) {
	
	if(!selectStartStation){
		if(window.confirm('是否将此站设为起点?')) {
			startStationSid = sid;
			selectStartStation = true;
		}
	}else if(!selectEndStation){
		if(window.confirm('是否将此站设为终点?')) {
			endStationSid = sid;
			selectEndStation = true;
			redirection(startStationSid,endStationSid);
		}
	}else {
		selectStartStation = false;
		selectEndStation = false;
	}
	
}

function getTransMessage(){
	
	var url = "TransMessageAction";
	var param = {
			s_sid : startStationSid,
			e_sid : endStationSid
	};
	
	document.title = "正在为您查询...";
	jQuery.ajax({
		url : url,
		type : "post",
		dataType : "text",
		data : param,
		success : function(data) {
			document.title = "微铁 | 换乘方案查询";
			alert(data);
			selectStartStation = false;
			selectEndStation = false;
		},
		error : function(data) {
			document.title = "微铁 | 换乘方案查询";
			alert('数据加载失败，请重试');
			selectStartStation = false;
			selectEndStation = false;
		},
	});
	
}

function redirection(s_sid,e_sid){
	document.title = "正在为您查询...";
	selectStartStation = false;
	selectEndStation = false;
	location.href = "trans.jsp?s_sid="+s_sid+"&e_sid="+e_sid;
}


