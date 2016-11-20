<%@page import="com.subway.baidumap.BaiduMapSubwayApi"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.subway.dao.SubwayDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%
	String startStationSid = request.getParameter("s_sid");
	String endStationSid = request.getParameter("e_sid");
%>

<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<title>微铁 | 推荐换乘方案</title>
<script src="js/mui.min.js"></script>
<script src="js/jquery.min.js"></script>
<link href="css/mui.min.css" rel="stylesheet" />
<link href="css/style.css" rel="stylesheet" />
<script type="text/javascript" charset="utf-8">
	<%
		if(startStationSid==null || startStationSid.trim().equals("") || endStationSid==null || endStationSid.trim().equals("")){
			out.print("location.href = 'trans.html'");
		}
	%>
</script>

<%
	SubwayDao subwayDao = new SubwayDao();
	String start_station_name = subwayDao.quaryStationNameBySid(startStationSid);
	String end_station_name = subwayDao.quaryStationNameBySid(endStationSid);
%>

</head>

<body class="bgpink">

		<header class="mui-bar mui-bar-nav bgblue jiaoxiaohead">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left "></a>
			<h1 class="mui-title">
			<%
				if(start_station_name==null || end_station_name==null){
					out.print("未找到站点数据");
				}else{
					out.print(start_station_name + "站到" + end_station_name + "站换乘方案");
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
				<li class="mui-table-view-cell"
					style="padding-top: 30px; padding-bottom: 30px; text-align: center;">
					
					<div id="segmentedControl" class="mui-segmented-control xingqonox" style="border-left: none; border-right: none;">
						<a class="mui-control-item mui-active xingqili"
							href="#item1mobile" style="border-radius:8px;text-align: left;border-left: 0px solid #dfdcdc !important;padding: 6px;">
							<div class="xingqi">起点</div>
							<div class="riqi"><strong>
							<%
								if(start_station_name==null){
									out.print("未找到站点数据");
								}else{
									out.print(start_station_name+"站");
								}
							%>
							</strong></div>
						</a> 
						<a class="mui-control-item xingqili" href="#item2mobile"  style="border-radius:8px;text-align: right;border-left: 0px solid #dfdcdc !important;padding: 6px;">
							<div class="xingqi">终点</div>
							<div class="riqi"><strong>
								<%
									if(end_station_name==null){
										out.print("未找到站点数据");
									}else{
										out.print(end_station_name+"站");
									}
								%>
							</strong></div>
						</a> 

					</div>
					
				</li>

				<li class="mui-table-view-cell"
					style="padding-top: 50px; padding-bottom: 50px;">
					<h4>从<%
								if(start_station_name==null){
									out.print("未找到站点数据");
								}else{
									out.print(start_station_name+"站");
								}
							%> 到 <%
									if(end_station_name==null){
										out.print("未找到站点数据");
									}else{
										out.print(end_station_name+"站");
									}
								%> 换乘方案：</h4>
								
							<%
								ArrayList<String> list = BaiduMapSubwayApi.queryTransMessage(start_station_name, end_station_name);
								for(int i=0;i<list.size();i++){
									out.println("<br><span class='mbiaoqian bg" + (i+1)%6 + "'>" + (i+1) + "</span>" + list.get(i)+"<br>");
								}
							%>
					
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