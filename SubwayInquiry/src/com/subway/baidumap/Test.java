package com.subway.baidumap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.subway.http.HttpClientConnectionManager;
import com.subway.util.StreamTool;

public class Test {
	
	public static void main(String[] args) throws Exception{

		SnCal snCal = new SnCal();

		Map<String, String> paramsMap = new LinkedHashMap<String, String>();
		paramsMap.put("mode", "transit");
		paramsMap.put("origin", "清华大学");
		paramsMap.put("destination", "中央民族大学");
		paramsMap.put("origin_region", "北京");
		paramsMap.put("destination_region", "北京");
		paramsMap.put("output", "json");
		paramsMap.put("ak", INFO.AK);
		
		/*
		String paramsStr = snCal.toQueryString(paramsMap);
		
		String wholeStr = new String("/direction/v1?" + paramsStr + INFO.SK);
		
		String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
		String SN = snCal.MD5(tempStr);
		System.out.println(SN);
		*/
		StringBuffer queryString = new StringBuffer();
		for (Entry<String, String> pair : paramsMap.entrySet()) {
			queryString.append(pair.getKey() + "=" + pair.getValue() + "&");
		}
		// queryString.append("sn="+SN);
		
		
		URL url = new URL(INFO.URL + "?" + queryString);
		//System.out.println(url.toString());
		
		/**
		 * 	origin	起点
			destination	终点
			tactics_incity	市内公交换乘策略(0 推荐；1 少换乘；2 少步行；3 不坐地铁；4 时间短；5 地铁优先)
			output	输出类型(可选 json 或xml 默认为json)
			page_size	返回每页几条路线(1-10)
			
			
			
			
		
		URL u2 = new URL("http://api.map.baidu.com/direction/v2/transit?origin=39.997031,116.393771&destination=39.967031,116.323771&tactics_incity=5&page_size=1"+"&ak="+INFO.AK);
		URL u3 = new URL("http://api.map.baidu.com/place/v2/suggestion?query=中央民族大学&region=北京&output=json"+"&ak="+INFO.AK);
		// http://api.map.baidu.com/direction/v1?mode=driving&origin=清华大学&destination=北京大学&origin_region=北京&destination_region=北京&output=json&ak=您的ak
		
		HttpURLConnection conn = (HttpURLConnection) u2.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(60000);
		conn.setReadTimeout(20000);

		InputStream inStream = conn.getInputStream();
		String res = new String(StreamTool.readInputStream(inStream),
				"utf-8");
		
		System.out.println(res);
		
		inStream.close();
		conn.disconnect();
		 */
		
		
		
		String res = HttpClientConnectionManager.httpGET("http://api.map.baidu.com/direction/v1?mode=driving&origin=清华大学&destination=北京大学&origin_region=北京&destination_region=北京&output=json&ak=" + INFO.AK); 
		System.out.println(res);
		
		/*JSONObject root = new JSONObject(res);
		JSONArray jsonArrayResult = root.getJSONArray("results");
		String[] nearStations = new String[jsonArrayResult.length()];
		for(int i=0;i<jsonArrayResult.length();i++){
			nearStations[i] = jsonArrayResult.getJSONObject(i).getString("name");
		}*/
		
		
	}

}
