<%@page import="com.subway.dao.SubwayDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%
	String sid = request.getParameter("sid");
%>

<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />

<script type="text/javascript" charset="utf-8">
	<%
		if(sid==null || sid.trim().equals("")){
			out.print("location.href = 'exit.html'");
		}
	%>
</script>
<script src="js/mui.min.js"></script>
<script src="js/jquery.min.js"></script>
<link href="css/mui.min.css" rel="stylesheet" />
<link href="css/style.css" rel="stylesheet" />
<script type="text/javascript" src="files/exit.js"></script>

<%
	SubwayDao subwayDao = new SubwayDao();
	String station_name = subwayDao.quaryStationNameBySid(sid);
	
%>
<title>微铁 | <%
	if (station_name == null) {
		out.print("未找到站点数据");
	} else {
		out.print(station_name + "站出口查询");
	}
%></title>

</head>

<body class="bgpink">

		<header class="mui-bar mui-bar-nav bgblue jiaoxiaohead">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left "></a>
			<h1 class="mui-title">
			<%
				if (station_name == null) {
					out.print("未找到站点数据");
				} else {
					out.print(station_name + "站");
				}
			%>
		</h1>
		</header>
		
		<div class="mui-content padding4 bgpink" style="background-color: #ffffff;">
			<div id="slider" class="mui-slider jxslider">
				<div class="mui-slider-group mui-slider-loop">
					<!-- 第一张 -->
					<div class="mui-slider-item">
						<img src="images/map1.jpg">
					</div>
				</div>
	
			</div>

			<div id="item1mobile" style="min-height: 450px;"
						class="mui-slider-item mui-control-content mui-active">
						<ul class="mui-table-view kcslidermainli">
							<li class="mui-table-view-cell" style="padding-top: 30px;padding-bottom: 30px;text-align: center;">
								<input id="m_text" type="text" class="textk" value="请输入目的地名称" onfocus="javascript:if(this.value=='请输入目的地名称')this.value='';">
								<div id="m_btn_search" class="peplckbtn" style="width: 100px;margin: 0 auto;"><a id="m_btn_text" style="border: 1px solid #4563fa;color:#4563fa;font-size: 13px;padding-top: 8px;padding-bottom: 8px;">查 询</a></div>
							</li>

							<li id="m_result1" class="mui-table-view-cell" style="padding-top: 50px;padding-bottom: 50px;display: none;">
								<h4 id="m_title"></h4><br>
								<div id="m_content"></div><br>
								<div id="m_walk"></div>
								<!-- 
								<div style="display: block;margin-top: 5px;margin-bottom: 5px;"><span class="mbiaoqian2 bg1">1</span>从 起点 向西南方向出发,走60米, 左转</div>
								<div style="display: block;margin-top: 5px;margin-bottom: 5px;"><span class="mbiaoqian2 bg2">2</span>走40米, 左转 进入 魏公村路</div>
								<div style="display: block;margin-top: 5px;margin-bottom: 5px;"><span class="mbiaoqian2 bg3">3</span>沿 魏公村路 走110米</div>
								<div style="display: block;margin-top: 5px;margin-bottom: 5px;"><span class="mbiaoqian2 bg4">4</span>右转 进入 中关村南大街辅路</div>
								<div style="display: block;margin-top: 5px;margin-bottom: 5px;"><span class="mbiaoqian2 bg5">5</span>沿 中关村南大街辅路 走260米, 直走 进入 中关村南大街</div>
								<div style="display: block;margin-top: 5px;margin-bottom: 5px;"><span class="mbiaoqian2 bg6">6</span>沿 中关村南大街 走250米, 右转</div>
								 -->
							</li>
							
							<li id="m_result2" class="mui-table-view-cell" style="padding-top: 50px;padding-bottom: 50px;display: none;">
								<h4 id="m_title2"></h4><br>
								<div id="m_content2"></div><br>
								<div id="m_walk2"></div>
							</li>
							
						</ul>
					</div>

		</div>

		<nav class="mui-bar mui-bar-tab footernav " style="background-color: #ffffff;border-top: 1px solid #cccccc;">
			<a class=" footernavli mui-active " href="time.html"> 
				<span class="footernavimg">
					<img src="images/time0.png">
				</span> 
				<span class="mui-tab-label footernavtxt">时刻查询</span>
			</a> 
			
			<a class=" footernavli mui-active " href="exit.html"> 
				<span class="footernavimg">
					<img src="images/user7.png">
				</span> 
				<span class="mui-tab-label footernavtxt">出口查询</span>
			</a>
			
			<a class=" footernavli mui-active " href="trans.html"> 
				<span class="footernavimg">
					<img src="images/footer1.png">
				</span> 
				<span class="mui-tab-label footernavtxt">换乘查询</span>
			</a>

		</nav>
		
		</body>

</html>