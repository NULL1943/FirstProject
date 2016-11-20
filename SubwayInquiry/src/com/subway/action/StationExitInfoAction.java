package com.subway.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.subway.baidumap.BaiduMapSubwayApi;
import com.subway.bean.StationExitResult;
import com.subway.dao.SubwayDao;
import com.subway.dao.TimeTableDao;

/**
 * 地铁出口查询
 * @author NULL
 *
 */
public class StationExitInfoAction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    public StationExitInfoAction() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("[地铁出口查询]");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");

		String sid = request.getParameter("sid");
		String keyword = request.getParameter("key");
		
		System.out.println("sid="+sid);
		System.out.println("keyword="+keyword);
        
        String outString = "";
		
		SubwayDao subwayDao = new SubwayDao();
		String station_name = subwayDao.quaryStationNameBySid(sid);
		StationExitResult ser = null;
		
		if(station_name == null){
			ser = new StationExitResult();
			ser.setTitle("未找到相关数据：");
			ser.setDescribe("请输入目的地全称或选择其他站点。");
		}else{
			try {
				ser = BaiduMapSubwayApi.quaryExitInfo(keyword, station_name);
			} catch (JSONException e) {
				e.printStackTrace();
				ser = new StationExitResult();
				ser.setTitle("未找到相关数据：");
				ser.setDescribe("请输入目的地全称或选择其他站点。");
			}
		}
		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("title", ser.getTitle());
			jsonObject.put("describe", ser.getDescribe());
			jsonObject.put("walk", ser.getWalkRoute());
			
			jsonObject.put("recommend", ser.getRecommend());
			
			jsonObject.put("title2", ser.getTitle2());
			jsonObject.put("describe2", ser.getDescribe2());
			jsonObject.put("walk2", ser.getWalkRoute2());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.out.println(jsonObject.toString());
		
		PrintWriter out = response.getWriter();
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

}
