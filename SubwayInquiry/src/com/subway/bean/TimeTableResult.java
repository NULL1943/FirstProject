package com.subway.bean;

import java.util.ArrayList;

/**
 * 地铁时刻查询结果实体类
 * @author NULL
 *
 */
public class TimeTableResult {
	
	private String station_and_direction_name;
	private ArrayList<String> data;
	public String getStation_and_direction_name() {
		return station_and_direction_name;
	}
	public void setStation_and_direction_name(String station_and_direction_name) {
		this.station_and_direction_name = station_and_direction_name;
	}
	public ArrayList<String> getData() {
		return data;
	}
	public void setData(ArrayList<String> data) {
		this.data = data;
	}
	
	
	

}
