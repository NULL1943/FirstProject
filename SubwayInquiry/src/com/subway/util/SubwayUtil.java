package com.subway.util;

import java.text.DecimalFormat;

import com.sun.corba.se.spi.orb.StringPair;

public class SubwayUtil {
	
	/**
	 * 两点测距
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double distaceOfTowPoints(double x1,double y1,double x2,double y2){
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}
	

	
	/**
	 * 根据经纬度计算两点间的实际距离
	 * 返回：米
	 * @return
	 */
	public static String getDistanceByLatLng(double lat1,  double lng1,  double lat2,  double lng2){

		if( ( Math.abs( lat1 ) > 90  ) ||(  Math.abs( lat2 ) > 90 ) ){
			return "-1";
		}
		
		if( ( Math.abs( lng1 ) > 180  ) ||(  Math.abs( lng2 ) > 180 ) ){
			return "-2";
		}
		
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));

		s = s * 6378.137 ;// EARTH_RADIUS;
		//s = Math.round(s * 10000) / 10000;
		DecimalFormat df = new DecimalFormat("######0");   
		return df.format(s * 1000);

	}
	
	/**
	 * 坐标转换
	 * @param d
	 * @return
	 */
	public static double rad(double d) {
		return d * Math.PI / 180.0;
	}
	
	public static void main(String[] agrs){
		/*
		System.out.println("国家图书馆地铁站-D口->"+distaceOfTowPoints(39.956579, 116.327024, 39.948124, 116.3315));
		System.out.println("国家图书馆地铁站-C口->"+distaceOfTowPoints(39.956579, 116.327024, 39.948321, 116.332215));
		System.out.println("国家图书馆地铁站-B口->"+distaceOfTowPoints(39.956579, 116.327024, 39.949541, 116.331885));
		System.out.println("国家图书馆地铁站-A口->"+distaceOfTowPoints(39.956579, 116.327024, 39.949484, 116.331343));
		System.out.println("魏公村地铁站-D口->"+distaceOfTowPoints(39.956579, 116.327024, 39.962957, 116.329423));
		System.out.println("魏公村地铁站-B口->"+distaceOfTowPoints(39.956579, 116.327024, 39.964167, 116.330096));
		System.out.println("魏公村地铁站-A口->"+distaceOfTowPoints(39.956579, 116.327024, 39.964065, 116.329417));
		*/
		System.out.println(getDistanceByLatLng(39.949484, 116.331343, 39.956579, 116.327024));
	}

}
