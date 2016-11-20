package com.subway.bean;

/**
 * 列车时刻表实体类
 * @author NULL
 *
 */
public class TimeTable {
	
	private int id;
	private String line_name;
	private String station_name;
	private String direction;
	private String time_table;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLine_name() {
		return line_name;
	}
	public void setLine_name(String line_name) {
		this.line_name = line_name;
	}
	public String getStation_name() {
		return station_name;
	}
	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getTime_table() {
		return time_table;
	}
	public void setTime_table(String time_table) {
		this.time_table = time_table;
	}
	@Override
	public String toString() {
		return "TimeTable [id=" + id + ", line_name=" + line_name
				+ ", station_name=" + station_name + ", direction=" + direction
				+ ", time_table=" + time_table + "]";
	}
	
	
	
}
