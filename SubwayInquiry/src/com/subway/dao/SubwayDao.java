package com.subway.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.subway.db.SQLUtil;

/**
 * 地铁数据相关基础查询
 * @author NULL
 *
 */
public class SubwayDao {

	/**
	 * 根据站点sid获取站点名称
	 * @param sid
	 * @return
	 */
	public String quaryStationNameBySid(String sid){
		
		String nameString = null;
		String sql = "SELECT _name FROM t_station WHERE _sid = ?";
		ResultSet resultSet = SQLUtil.executeQuery(sql,new Object[]{sid});
		try {
			if(resultSet.next()){
				nameString = resultSet.getString("_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return nameString;
		
	}
	
	/**
	 * 找到同一条线上的临近一站站名
	 * @param sid
	 * @return
	 */
	public String quaryBrotherStationName(String station_name){
		
		String nameString = null;
		ArrayList<String> list = new ArrayList<>();
		String sql = "SELECT _station_name FROM t_time_table WHERE _line_name IN "
				+ " (SELECT _line_name FROM t_time_table WHERE _station_name = ?) ";
		ResultSet resultSet = SQLUtil.executeQuery(sql,new Object[]{station_name});
		try {
			while(resultSet.next()){
				// nameString = resultSet.getString("_station_name");
				list.add(resultSet.getString("_station_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int index = list.indexOf(station_name);
		if(index == 0){
			nameString = list.get(1);
		}else if(index >= list.size()-2){
			nameString = list.get(list.size()-4);
		}else{
			nameString = list.get(index+2);
		}
		
		return nameString;
		
	}
	
	
	public static void main(String[] args) {
		SubwayDao dao = new SubwayDao();
		System.out.println(dao.quaryBrotherStationName("魏公村"));
	}
}
