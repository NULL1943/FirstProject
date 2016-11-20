package com.subway.bean;

/**
 * 地点信息实体
 * 
 * @author NULL
 *
 */
public class PlaceInfo {
	
    private String name;
    private double lat;// 纬度 39.956579
    private double lng;// 经度  116.327024
    private String city;
    private String district;
    private String cityid;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	@Override
	public String toString() {
		return "PlaceInfo [name=" + name + ", lat=" + lat + ", lng=" + lng
				+ ", city=" + city + ", district=" + district + ", cityid="
				+ cityid + "]";
	}
    
    

}
