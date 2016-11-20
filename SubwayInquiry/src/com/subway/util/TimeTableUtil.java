package com.subway.util;

import java.util.Date;

import com.subway.dao.TimeTableDao;

public class TimeTableUtil {

	/**
	 * �����г�ʱ�̱��ַ������ݽ��������һ���г�ʱ��
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
		
		// �ж��Ƿ��Ѿ�ͣ��
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
	 * �����г�ʱ�̱��ַ������ݽ��������һ���г�ʱ��
	 * @param time_table
	 * @return
	 */
	public static String formatTime(String time_table,int mH,int mM) {
		
		String str = "";
		
		if(StringUtil.isEmpty(time_table)){
			str = "����ȱʧ";
			return str;
		}
		
		String[] array = time_table.split(";");

		// �ж��Ƿ��Ѿ�ͣ�ˣ�ͣ���򷵻ص�һ���г�ʱ��
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
	 * �����г�ʱ�̱��ַ������ݽ������currentHourʱcurrentMinu�ֺ����һ���г�ʱ��
	 * @param time_table
	 * @return
	 * res[0] ���Сʱ��
	 * res[1] ���������
	 */
	public static int[] formatTimeNext(String station_name,String subway_direction,int currentHour,int currentMinu) {
		
		int[] res = new int[2];
		TimeTableDao dao = new TimeTableDao();
		String time_table = dao.getStationTimeTable(station_name, subway_direction);
		
		if(time_table == null){
			System.err.println("ERROR! =>>=>>��t_time_table��û���ҵ�" +station_name+"վ" + subway_direction + "��ʱ�̱�");
			res[0] = -1;
			res[1] = -1;
			return res;
		}
		
		String[] array = time_table.split(";");
		int mH = currentHour;
		int mM = currentMinu;
		
		// �ж��Ƿ��Ѿ�ͣ��
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
	 * ʱ��ӷ�
	 * @param currentHour
	 * @param currentMinu
	 * @param duration
	 * @return
	 * res[0] ���Сʱ��
	 * res[1] ���������
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
