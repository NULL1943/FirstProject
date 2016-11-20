package com.subway.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subway.dao.TimeTableDao;

/**
 * 查新列车时刻表
 * @author NULL
 *
 */
public class TimeTableAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public TimeTableAction() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("[查新列车时刻表]");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");

        String sid = request.getParameter("sid");
		
		TimeTableDao dao = new TimeTableDao();
		String str = dao.getStationTimeTable(sid);
		
		// String jsonString = JSONSerializer.toJSON(result).toString();
		
		PrintWriter out = response.getWriter();
		
		out.println(str);
		out.flush();
		out.close();
		
	}

}
