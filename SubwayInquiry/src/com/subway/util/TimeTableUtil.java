package com.subway.util;

import java.util.Date;

import com.subway.dao.TimeTableDao;

public class TimeTableUtil {

	/**
	 * 根据列车时刻表字符串数据解析获得下一班列车时刻
	 * @param time_table
	 * @return
	 */
	public static String formatTime(String time_table) {
		
		String str = "";
		
		if(StringUtil.isEmpty(time_table)){
			str = "0:0";
			return str;
		}
		
		String[] array = time_table.split(";");
		Date date = new Date();
		int mH = date.getHours();
		int mM = date.getMinutes();
		
		// 判断是否已经停运
		int sH = Integer.valueOf(array[0].split(":")[0]);
		int sM = Integer.valueOf(array[0].split(":")[1]);
		int eH = Integer.valueOf(array[array.length-1].split(":")[0]);
		int eM = Integer.valueOf(array[array.length-1].split(":")[1]);
		if((mH > eM && mH < sH) || (mH == sH && mM < sM) || (mH == eH && mM > eM)){
			str = array[0];
			return str;
		}
	
		/*System.out.println(mH);
		System.out.println(mM);*/
		
		for(String s : array){

			Integer h = Integer.valueOf(s.split(":")[0]);
			Integer m = Integer.valueOf(s.split(":")[1]);
			
			if(h < mH){
				continue;
			}else if(h == mH){
				if(m < mM){
					continue;
				}else{
					str = s;
					break;
				}
			}else{
				str = s;
				break;
			}

		}
		
		return str;
		
	}
	
	/**
	 * 根据列车时刻表字符串数据解析获得下一班列车时刻
	 * @param time_table
	 * @return
	 */
	public static String formatTime(String time_table,int mH,int mM) {
		
		String str = "";
		
		if(StringUtil.isEmpty(time_table)){
			str = "参数缺失";
			return str;
		}
		
		String[] array = time_table.split(";");

		// 判断是否已经停运：停运则返回第一班列车时刻
		int sH = Integer.valueOf(array[0].split(":")[0]);
		int sM = Integer.valueOf(array[0].split(":")[1]);
		int eH = Integer.valueOf(array[array.length-1].split(":")[0]);
		int eM = Integer.valueOf(array[array.length-1].split(":")[1]);
		if((mH > eM && mH < sH) || (mH == sH && mM < sM) || (mH == eH && mM > eM)){
			str = array[0];
			return str;
		}
	
		/*System.out.println(mH);
		System.out.println(mM);*/
		
		for(String s : array){

			Integer h = Integer.valueOf(s.split(":")[0]);
			Integer m = Integer.valueOf(s.split(":")[1]);
			
			if(h < mH){
				continue;
			}else if(h == mH){
				if(m <= mM){
					continue;
				}else{
					str = s;
					break;
				}
			}else{
				str = s;
				break;
			}

		}
		
		return str;
		
	}
	
	/**
	 * 根据列车时刻表字符串数据解析获得currentHour时currentMinu分后的下一班列车时刻
	 * @param time_table
	 * @return
	 * res[0] 结果小时数
	 * res[1] 结果分钟数
	 */
	public static int[] formatTimeNext(String station_name,String subway_direction,int currentHour,int currentMinu) {
		
		int[] res = new int[2];
		TimeTableDao dao = new TimeTableDao();
		String time_table = dao.getStationTimeTable(station_name, subway_direction);
		
		if(time_table == null){
			System.err.println("ERROR! =>>=>>在t_time_table中没有找到" +station_name+"站" + subway_direction + "的时刻表");
			res[0] = -1;
			res[1] = -1;
			return res;
		}
		
		String[] array = time_table.split(";");
		int mH = currentHour;
		int mM = currentMinu;
		
		// 判断是否已经停运
		int sH = Integer.valueOf(array[0].split(":")[0]);
		int sM = Integer.valueOf(array[0].split(":")[1]);
		int eH = Integer.valueOf(array[array.length-1].split(":")[0]);
		int eM = Integer.valueOf(array[array.length-1].split(":")[1]);
		if((mH > eM && mH < sH) || (mH == sH && mM < sM) || (mH == eH && mM > eM)){
			res[0] = -2;
			res[1] = -2;
			return res;
		}
		
		for(String s : array){

			Integer h = Integer.valueOf(s.split(":")[0]);
			Integer m = Integer.valueOf(s.split(":")[1]);
			
			if(h < mH){
				continue;
			}else if(h == mH){
				if(m < mM){
					continue;
				}else{
					res[0] = h;
					res[1] = m;
					break;
				}
			}else{
				res[0] = h;
				res[1] = m;
				break;
			}

		}
		
		return res;
	}
	
	
	/**
	 * 时间加法
	 * @param currentHour
	 * @param currentMinu
	 * @param duration
	 * @return
	 * res[0] 结果小时数
	 * res[1] 结果分钟数
	 * 
	 */
	public static int[] timePlus(int currentHour,int currentMinu,int duration){
		
		int[] res = new int[2];
		
		int carry = 0;
		if(currentMinu+duration>=60){
			carry = (currentMinu+duration) / 60;
			res[1] = (currentMinu+duration) % 60;
		}else{
			res[1] = currentMinu+duration;
		}
		
		currentHour += carry;
		if(currentHour>=24){
			res[0] = currentHour-24;
		}else{
			res[0] = currentHour;
		}

		System.out.println(currentHour+":"+currentMinu + " + " + duration + " = " + res[0]+":"+res[1] );
		
		return res;
		
	}
	
	public static void main(String[] args){
		/*
		String data = "21:35;22:37;21:39;21:41;21:49;21:53";
		System.out.println(formatTime(data));
		*/
		timePlus(23,30,30);
		
	}
}
