var sid = getUrlParam("sid");

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
			// 点击查询按钮
			$("#m_btn_search").click(
					function() {
						
						var key = $("#m_text").val();
						if(key == "" || key == null || key == "null" || key=="请输入目的地名称"){
							alert("目的地名称不能为空");
							return;
						}
						getStationExitInfo(sid,key);
						
					});
			
		});

// 获取url中的参数
function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg); // 匹配目标参数
	if (r != null)
		return unescape(r[2]);
	return null; // 返回参数值
}

function redirection(sid){
	document.title = "正在为您查询...";
	location.href = "exit.jsp?sid="+sid;
}

function getStationExitInfo(sid,key){

	var url = "StationExitInfoAction";
	var param = {
			sid : sid,
			key : key
	};

	$('#m_btn_text').html("正在查询...");
	
	jQuery.ajax({
		url : url,
		type : "post",
		dataType : "json",
		data : param,
		success : function(data) {
			
			$('#m_btn_text').html("查 询");
			
			$("#m_result1").css("display","block");
			$('#m_title').html(data['title']);
			$('#m_content').html(data['describe']);
			var steps = data['walk'].split("#");
			var walk = "";
			for(var i=0;i<steps.length-1;i++){
				walk += "<div style='display: block;margin-top: 5px;margin-bottom: 5px;font-size:12px;'><span class='mbiaoqian2 bg" + (i+1) + "'>" + (i+1) + "</span>" + steps[i] + "</div>";
			}
			if(data['recommend'] == 0){
				walk += "<br><br>";
			}
			$('#m_walk').html(walk);
			
			if(data['recommend'] == 1) {
				
				$("#m_result2").css("display","block");
				$('#m_title2').html(data['title2']);
				$('#m_content2').html(data['describe2']);
				var steps = data['walk2'].split("#");
				var walk = "";
				for(var i=0;i<steps.length-1;i++){
					walk += "<div style='display: block;margin-top: 5px;margin-bottom: 5px;font-size:12px;'><span class='mbiaoqian2 bg" + (i+1) + "'>" + (i+1) + "</span>" + steps[i] + "</div>";
				}
				walk += "<br><br>";
				$('#m_walk2').html(walk);
			}
			
		},
		error : function(data) {
			$('#m_btn_text').html("查 询");
			alert('数据加载失败，请重试');
		},
	});
}
