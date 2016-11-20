<%@page import="com.subway.bean.TimeTableResult"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.subway.dao.TimeTableDao"%>
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

<script src="js/mui.min.js"></script>
<script src="js/jquery.min.js"></script>
<link href="css/mui.min.css" rel="stylesheet" />
<link href="css/style.css" rel="stylesheet" />
<script type="text/javascript" charset="utf-8">
<%
	if(sid==null || sid.trim().equals("")){
		out.print("location.href = 'time.html'");
	}
%>

</script>

<%
	SubwayDao subwayDao = new SubwayDao();
	String station_name = subwayDao.quaryStationNameBySid(sid);
%>

<title>微铁 | <%
	if (station_name == null) {
		out.print("未找到站点数据");
	} else {
		out.print(station_name + "站地铁时刻");
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
			
			<%
				TimeTableDao dao = new TimeTableDao();
				ArrayList<TimeTableResult> list = dao.getStationTimeTableList(sid);
			%>

			<div id="item1mobile" style="min-height: 450px;"
						class="mui-slider-item mui-control-content mui-active">
						<ul class="mui-table-view kcslidermainli">
							
								<%
									
									for(int i=0;i<list.size();i++){
										out.println("<li class='mui-table-view-cell'>");
										out.println("<span class='mbiaoqian bg" + (i+1) + "'>"+(i+1)+"</span>"+list.get(i).getStation_and_direction_name()+"<br>");
										
										for(int j=0;j<list.get(i).getData().size();j++){
											if((j+1)%3 == 0){
												out.println("<span class='mbiaoqian'></span>"+list.get(i).getData().get(j)+"<br>");
											}else{
												out.println("<span class='mbiaoqian'></span>"+list.get(i).getData().get(j));
											}
										}
									}
									
								%>

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