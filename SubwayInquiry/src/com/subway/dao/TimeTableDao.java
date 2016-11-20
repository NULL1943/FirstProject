package com.subway.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.subway.bean.TimeTable;
import com.subway.bean.TimeTableResult;
import com.subway.db.SQLUtil;
import com.subway.util.TimeTableUtil;

/**
 * 列车时刻查询数据库操作类
 * @author NULL
 *
 */
public class TimeTableDao {
	
	/**
	 * 根据站点id获得站点时刻数据
	 * @param sid
	 * @return
	 */
	public String getStationTimeTable(String sid){

		String str = "";
		String sql = "SELECT * FROM t_time_table WHERE _station_name IN ("
				+ "SELECT _name FROM t_station WHERE _sid = ?"
				+ ")";
		ResultSet resultSet = SQLUtil.executeQuery(sql, new Object[]{sid});
		
		ArrayList<TimeTable> list = new ArrayList<>();
		try {
			while(resultSet.next()){
				TimeTable t = new TimeTable();
				t.setId(resultSet.getInt("_id"));
				t.setLine_name(resultSet.getString("_line_name"));
				t.setStation_name(resultSet.getString("_station_name"));
				t.setDirection(resultSet.getString("_direction"));
				t.setTime_table(resultSet.getString("_time_table"));
				list.add(t);
				// System.out.println(t.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(list.size() == 0){
			str = "暂无此站点数据";
		}else{
			str += list.get(0).getStation_name()+"站\n\n";
			for(TimeTable t : list){
				str += t.getLine_name() + " " + t.getDirection() + "  " + TimeTableUtil.formatTime(t.getTime_table()) + "\n";
			}
		}
		
		return str;
	}
	
	/**
	 * 根据站点id获得站点时刻数据
	 * @param sid
	 * @return
	 */
	public ArrayList<TimeTableResult> getStationTimeTableList(String sid){

		ArrayList<TimeTableResult> listResult = new ArrayList<>();
		
		String sql = "SELECT * FROM t_time_table WHERE _station_name IN ("
				+ "SELECT _name FROM t_station WHERE _sid = ?"
				+ ")";
		ResultSet resultSet = SQLUtil.executeQuery(sql, new Object[]{sid});
		
		ArrayList<TimeTable> list = new ArrayList<>();
		try {
			while(resultSet.next()){
				TimeTable t = new TimeTable();
				t.setId(resultSet.getInt("_id"));
				t.setLine_name(resultSet.getString("_line_name"));
				t.setStation_name(resultSet.getString("_station_name"));
				t.setDirection(resultSet.getString("_direction"));
				t.setTime_table(resultSet.getString("_time_table"));
				list.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(list.size() == 0){
			return listResult;
		}else{
			for(TimeTable t : list){
				TimeTableResult ttr = new TimeTableResult();
				ttr.setStation_and_direction_name(t.getLine_name() + " " + t.getDirection());
				ArrayList<String> times = new ArrayList<String>();
				ttr.setData(times);
				// 获取第1班列车
				Date date = new Date();
				int mH = date.getHours();
				int mM = date.getMinutes();
				String time_1 = TimeTableUtil.formatTime(t.getTime_table(), mH, mM);
				times.add(time_1);
				// 获取第2班列车
				String time_2 = TimeTableUtil.formatTime(t.getTime_table(), Integer.valueOf(time_1.split(":")[0]), Integer.valueOf(time_1.split(":")[1]));
				times.add(time_2);
				// 获取第3班列车
				String time_3 = TimeTableUtil.formatTime(t.getTime_table(), Integer.valueOf(time_2.split(":")[0]), Integer.valueOf(time_2.split(":")[1]));
				times.add(time_3);
				// 获取第4班列车
				String time_4 = TimeTableUtil.formatTime(t.getTime_table(), Integer.valueOf(time_3.split(":")[0]), Integer.valueOf(time_3.split(":")[1]));
				times.add(time_4);
				// 获取第5班列车
				String time_5 = TimeTableUtil.formatTime(t.getTime_table(), Integer.valueOf(time_4.split(":")[0]), Integer.valueOf(time_4.split(":")[1]));
				times.add(time_5);
				// 获取第6班列车
				String time_6 = TimeTableUtil.formatTime(t.getTime_table(), Integer.valueOf(time_5.split(":")[0]), Integer.valueOf(time_5.split(":")[1]));
				times.add(time_6);
				listResult.add(ttr);
			}
		}
		
		return listResult;
	}
	
	/**
	 * 根据站点名和方向获得站点时刻数据
	 * @param sid
	 * @return
	 */
	public String getStationTimeTable(String station_name,String subway_direction){

		String str = null;
		String sql = "SELECT _time_table FROM t_time_table WHERE _station_name = ? AND _direction = ?";
		ResultSet resultSet = SQLUtil.executeQuery(sql, new Object[]{station_name,subway_direction});
		
		try {
			if(resultSet.next()){
				str = resultSet.getString("_time_table");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return str;
	}
	
	public static void main(String[] args){
		TimeTableDao dao = new TimeTableDao();
		System.out.println(dao.getStationTimeTable("svgjsImage2275"));
	}

}
