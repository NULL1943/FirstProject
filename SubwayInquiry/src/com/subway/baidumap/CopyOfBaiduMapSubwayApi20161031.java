package com.subway.baidumap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.subway.bean.PlaceInfo;
import com.subway.dao.SubwayDao;
import com.subway.http.HttpClientConnectionManager;

/**
 * 百度地图公交API
 * 路径规划API:http://lbsyun.baidu.com/index.php?title=webapi/direction-api-v2
 * Place服务API：http://api.map.baidu.com/place/v2/suggestion/
 * 
 * @author NULL
 *
 */
public class CopyOfBaiduMapSubwayApi20161031 {
	
	/**
	 * 根据关键词查询地点信息
	 * @return
	 */
	public static PlaceInfo getPlaceInfo(String keyword){
		
		PlaceInfo p = new PlaceInfo();
		try {
			keyword=URLEncoder.encode(keyword,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String url = "http://api.map.baidu.com/place/v2/suggestion?query=" + keyword + "&region=北京&output=json"+"&ak="+INFO.AK;
		
		String result = HttpClientConnectionManager.httpGET(url);
		System.out.println(url);
		System.out.println(result);
		if(result == null){
			p.setName("未找到相关地点");
			p.setLat(-1);
			p.setLng(-1);
		}else{
			try {
				//System.out.println(result);
				JSONObject object = new JSONObject(result);
				JSONArray array = object.getJSONArray("result");
				if(array.length()==0){
					p.setName("未找到相关地点");
					p.setLat(-1);
					p.setLng(-1);
				}else{
					JSONObject obj = array.getJSONObject(0);
					p.setName(obj.getString("name"));
					
					JSONObject ll = obj.getJSONObject("location");
					p.setLat(ll.getDouble("lat"));
					p.setLng(ll.getDouble("lng"));
					
					p.setCity(obj.getString("city"));
					p.setDistrict(obj.getString("district"));
					p.setCityid(obj.getString("cityid"));
					
					// System.out.println(p.toString());
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return p;
		
	}
	
	/**
	 * 根据地点关键词查询地铁出站口信息
	 * @param keyword 关键词
	 * @param station_name 站点名称
	 * 
	 * 方法：
		1、根据关键词获取当前地点经纬度x,y
		2、取x+0.05,y+0.05作为起点，x,y作为终点，调用API查询路线规划；
		3、遍历结果中的instructions字段，取其中包含station_name的作为结果res；
		4、解析字串res，得到出口名（字串中最后一个）
		
	       返回：
		1、未找到相关数据
		2、 (title) xxx站去往yyy：\n
		   (body)  请从【w口】出
	 * 
	 * @return
	 */
	public static String quaryExitInfo(String keyword,String station_name){
		
		System.out.println("quaryExitInfo->keyword="+keyword);
		System.out.println("quaryExitInfo->station_name="+station_name);
		
		String exitString = null;
		PlaceInfo placeInfo = getPlaceInfo(keyword);
		// 找到同一条线上的临近一站站名
		SubwayDao dao = new SubwayDao();
		String brotherStation = dao.quaryBrotherStationName(station_name);
		System.out.println("quaryExitInfo->brotherStation="+brotherStation);
		
		PlaceInfo placeInfoStation = getPlaceInfo("地铁"+station_name+"站");
		//PlaceInfo placeInfoBrotherStation = getPlaceInfo("地铁"+brotherStation+"站");
		PlaceInfo placeInfoBrotherStation = getPlaceInfo("地铁安河桥北站-公交车站");
		
		System.out.println("quaryExitInfo->placeInfoStation="+placeInfo.toString());
		System.out.println("quaryExitInfo->placeInfoBrotherStation="+placeInfoBrotherStation.toString());
		
		if(placeInfo.getLat() == -1){
			return exitString;
		}
		
		double d_lat = placeInfo.getLat();
		double d_lng = placeInfo.getLng();
		//double o_lat = d_lat + 0.15;
		//double o_lng = d_lng + 0.15;
		double o_lat = placeInfoBrotherStation.getLat();
		double o_lng = placeInfoBrotherStation.getLng();
		
		String url = "http://api.map.baidu.com/direction/v2/transit?origin=" + o_lat  + "," + o_lng + "&destination=" + d_lat + "," + d_lng + "&page_size=10&page_index=3"+"&ak="+INFO.AK;
		String pathString = HttpClientConnectionManager.httpGET(url);
		System.out.println(pathString);
		
		// parse data
		try {
			JSONObject rootJsonObject = new JSONObject(pathString);
			JSONObject resultJsonObject = rootJsonObject.getJSONObject("result");
			JSONArray routesJsonArray = resultJsonObject.getJSONArray("routes");
			
			if(routesJsonArray.length() == 0){
				return exitString;
			}
			
			JSONArray stepsJsonArray = routesJsonArray.getJSONObject(0).getJSONArray("steps");
			boolean found = false;
			for(int i=stepsJsonArray.length()-1;i>=0;i--){
				
				String instructions = stepsJsonArray.getJSONArray(i).getJSONObject(0).getString("instructions");
				System.out.println(instructions);
				
				if(instructions.contains("地铁")){
					if(instructions.contains(station_name)){
						int index = instructions.lastIndexOf(station_name);
						String subString = instructions.substring(index, instructions.length());
						subString = subString.replace("(", " ").replace(")", " ");
						exitString = "从"+station_name+"站去往"+placeInfo.getName()+":\n\n"+"请从【"+subString+"】出";
					}else{
						exitString = "从 " + station_name + "站 不能直接到达 " + keyword + ",请参考推荐站点出口:\n\n";
						int index = instructions.lastIndexOf("到");
						String s = instructions.substring(index+1,instructions.length());
						exitString += s.replace("(", " ").replace(")", " ");
					}
					found = true;
					break;
				}
				
				
				
			}
			if(!found){
				exitString = "从 " + station_name + "站 不能直接到达 " + keyword + ",请选择其他站点。";
			}
			
			
			
			/*
			// 最后一站不包括所选地铁站
			if(!stepsJsonArray.getJSONArray(stepsJsonArray.length()-2).getJSONObject(0).getString("instructions").contains(station_name+"站") && !stepsJsonArray.getJSONArray(stepsJsonArray.length()-1).getJSONObject(0).getString("instructions").contains(station_name+"站")){
				
				String tempsString = stepsJsonArray.getJSONArray(stepsJsonArray.length()-2).getJSONObject(0).getString("instructions");
				if(tempsString.contains("站(")){
					exitString = "从 " + station_name + "站 不能直接到达 " + keyword + ",请参考推荐站点出口:\n\n";
					int i = tempsString.lastIndexOf("到");
					String s = tempsString.substring(i+1,tempsString.length());
					exitString += s.replace("(", " ").replace(")", " ");
				}else{
					exitString = "从 " + station_name + "站 不能直接到达 " + keyword + ",请选择其他站点。";
				}
				

			}else{
				for(int i=0;i<stepsJsonArray.length();i++){
					String instructions = stepsJsonArray.getJSONArray(i).getJSONObject(0).getString("instructions");
					if(instructions.contains(station_name) && instructions.contains("口)")){
						System.out.println(instructions);
						int index = instructions.lastIndexOf(station_name);
						System.out.println(index);
						String subString = instructions.substring(index, instructions.length());
						System.out.println(subString);
						
						subString = subString.replace("(", " ").replace(")", " ");
						exitString = "从"+station_name+"站去往"+placeInfo.getName()+":\n\n"+"请从【"+subString+"】出";
						
					}
				}
			}
			
			*/

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return exitString;
		
	}
	
	public static void main(String[] args){
		
		System.out.println(getPlaceInfo("北京理工大学"));
		//quaryExitInfo("北京理工大学","魏公村站");
	}

}
