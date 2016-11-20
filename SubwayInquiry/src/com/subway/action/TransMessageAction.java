package com.subway.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.subway.baidumap.BaiduMapSubwayApi;
import com.subway.dao.SubwayDao;

/**
 * 换乘方案查询
 * @author NULL
 *
 */
public class TransMessageAction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

    public TransMessageAction() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("[换乘方案查询]");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");

		String startStationSid = request.getParameter("s_sid");
		String endStationSid = request.getParameter("e_sid");
		
		// System.out.println("startStationSid=>>"+startStationSid);
		// System.out.println("endStationSid=>>"+endStationSid);
        
        String outString = "";
		
		SubwayDao subwayDao = new SubwayDao();
		String startStationName = subwayDao.quaryStationNameBySid(startStationSid);
		String endStationName = subwayDao.quaryStationNameBySid(endStationSid);
		
		//outString = BaiduMapSubwayApi.queryTransMessage(startStationName, endStationName);
		
		// outString = "起点是：" + startStationName + "\n终点是：" + endStationName;
		
		System.out.println(outString);
		
		PrintWriter out = response.getWriter();
		out.println(outString);
		out.flush();
		out.close();
	}

}
