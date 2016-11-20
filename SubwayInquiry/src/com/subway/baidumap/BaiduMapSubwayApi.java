package com.subway.baidumap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.subway.bean.PlaceInfo;
import com.subway.bean.StationExitResult;
import com.subway.http.HttpClientConnectionManager;
import com.subway.util.SubwayUtil;
import com.subway.util.TimeTableUtil;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * 百度地图公交API
 * 路径规划API:http://lbsyun.baidu.com/index.php?title=webapi/direction-api-v2
 * Place服务API：http://api.map.baidu.com/place/v2/suggestion/
 * 
 * @author NULL
 *
 */
public class BaiduMapSubwayApi {
	
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
		String url = "http://api.map.baidu.com/place/v2/suggestion?query=" + keyword + "&region=北京市&output=json"+"&ak="+INFO.AK;
		
		String result = HttpClientConnectionManager.httpGET(url);
		if(result == null){
			p.setName("未找到相关地点");
			p.setLat(-1);
			p.setLng(-1);
		}else{
			try {
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
	 * @throws JSONException 
	 * @throws UnsupportedEncodingException 
	 */
	public static StationExitResult quaryExitInfo(String keyword,String station_name) throws JSONException, UnsupportedEncodingException{
		
		StationExitResult ser = new StationExitResult();
		
		// Step1:查找关键词位置信息
		PlaceInfo placeInfo = getPlaceInfo(keyword);
		// System.out.println(placeInfo.toString());
		
		// 如果没有找到相应的出站口
		if (placeInfo.getLat() == -1) {
			ser.setStatus(StationExitResult.STATUS_FAIL);
			ser.setTitle("未找到地点 " + placeInfo.getName());
			ser.setDescribe("请输入正确的目的地名称。");
			return ser;
		}
		
		PlaceInfo stationExitMin = getStationExitPlaceInfo(station_name, placeInfo);
		if(stationExitMin == null){
			ser.setStatus(StationExitResult.STATUS_FAIL);
			ser.setTitle("未找到站点：" + station_name);
			ser.setDescribe("请重试。");
			return ser;
		}
		
		// Step4:计算得到步行方式和距离关系(From placeMin TO placeInfo)
		String[] walkRoute = getWalkRoute(stationExitMin,placeInfo);
		ser.setStatus(StationExitResult.STATUS_SUCC);
		ser.setTitle("从 "+station_name+"站 去往 "+placeInfo.getName()+"：");
		ser.setWalkRoute(walkRoute[0]);
		// String Distance = SubwayUtil.getDistanceByLatLng(stationExitMin.getLat(), stationExitMin.getLng(), placeInfo.getLat(), placeInfo.getLng());
		ser.setDescribe("请从"+stationExitMin.getName().toUpperCase()+"出，步行" + walkRoute[1] + "米即可到达。");
		
		// Step3:在目的地附近搜索地铁出口:如果所选地铁站不在附近地铁站列表中，则推荐.
		ser.setRecommend(0);
		boolean selectedStationIsNearest = false; // 所选地铁站是否为离目的地最近的地铁站
		String nearestStationName = null;
		String nearStationRes = HttpClientConnectionManager
				.httpGET("http://api.map.baidu.com/place/v2/search?q="
						+ placeInfo.getName()
						+ "&tag=地铁站&region=北京&filter=industry_type:life|sort_name:distance|sort_rule:1&output=json&ak="
						+ INFO.AK);
		JSONObject root = new JSONObject(nearStationRes);
		JSONArray jsonArrayResult = root.getJSONArray("results");
		String[] nearStations = new String[jsonArrayResult.length()];
		
		System.out.println("nearStationRes="+nearStationRes);
		
		if (jsonArrayResult.length() > 0) {
			
			nearestStationName = jsonArrayResult.getJSONObject(0).getString("name").replaceAll("地铁", "").replaceAll("站", "");
			System.out.println("nearestStationName="+nearestStationName);
			
			for (int i = 0; i < jsonArrayResult.length(); i++) {
				nearStations[i] = jsonArrayResult.getJSONObject(i).getString("name");
				if (nearStations[i].contains(station_name)) {
					selectedStationIsNearest = true;
				}
			}

			if (!selectedStationIsNearest) {
				stationExitMin = getStationExitPlaceInfo(nearestStationName,placeInfo);
				System.out.println("stationExitMin="+stationExitMin);
				if(stationExitMin != null) {
					ser.setRecommend(1);
					String[] walkRoute2 = getWalkRoute(stationExitMin,placeInfo);
					// String distance = SubwayUtil.getDistanceByLatLng(stationExitMin.getLat(), stationExitMin.getLng(), placeInfo.getLat(), placeInfo.getLng());
					ser.setTitle2("为您推荐：距离 " + placeInfo.getName() + " 最近的地铁站口是 " + stationExitMin.getName().toUpperCase() + "：");
					ser.setDescribe2("距离目的地 " + walkRoute2[1] + "米。");
					ser.setWalkRoute2(walkRoute2[0]);
				}
			}
		}

		return ser;
		
	}
	
	/**
	 * 查询地铁站station_name距离地点placeInfo最近的出站口
	 * 
	 * 查询方式：遍历该站所有出口
	 * 
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	public static PlaceInfo getStationExitPlaceInfo(String station_name,PlaceInfo placeInfo) throws UnsupportedEncodingException, JSONException {
		
		// Step2.1:查找该地铁站的所有出站口
		String url1 = "http://api.map.baidu.com/place/v2/suggestion?query=";
		String url2 = "&region=" + URLEncoder.encode("北京市", "UTF-8")
				+ "&city_limit=true&output=json&ak=" + INFO.AK;
		ArrayList<PlaceInfo> exitList = new ArrayList<PlaceInfo>();
		String exitResult = null;

		// A
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站a口", "UTF-8") + url2);
		JSONObject rootJsonObject = new JSONObject(exitResult);
		JSONArray resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("A=>>"+p.toString());
		}

		// a1
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站a1口", "UTF-8") + url2);
		rootJsonObject = new JSONObject(exitResult);
		resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("B=>>"+p.toString());
		}

		// a2
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站a2口", "UTF-8") + url2);
		rootJsonObject = new JSONObject(exitResult);
		resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("B=>>"+p.toString());
		}

		// b
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站b口", "UTF-8") + url2);
		rootJsonObject = new JSONObject(exitResult);
		resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("B=>>"+p.toString());
		}

		// b1
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站b1口", "UTF-8") + url2);
		rootJsonObject = new JSONObject(exitResult);
		resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("B=>>"+p.toString());
		}

		// b2
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站b2口", "UTF-8") + url2);
		rootJsonObject = new JSONObject(exitResult);
		resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("B=>>"+p.toString());
		}

		// c
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站c口", "UTF-8") + url2);
		rootJsonObject = new JSONObject(exitResult);
		resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("C=>>"+p.toString());
		}

		// c1
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站c1口", "UTF-8") + url2);
		rootJsonObject = new JSONObject(exitResult);
		resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("C=>>"+p.toString());
		}

		// c2
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站c2口", "UTF-8") + url2);
		rootJsonObject = new JSONObject(exitResult);
		resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("C=>>"+p.toString());
		}

		// d
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站d口", "UTF-8") + url2);
		rootJsonObject = new JSONObject(exitResult);
		resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("D=>>"+p.toString());
		}

		// d1
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站d1口", "UTF-8") + url2);
		rootJsonObject = new JSONObject(exitResult);
		resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("D=>>"+p.toString());
		}

		// d2
		exitResult = HttpClientConnectionManager.httpGET(url1
				+ URLEncoder.encode(station_name + "地铁站d2口", "UTF-8") + url2);
		rootJsonObject = new JSONObject(exitResult);
		resultArray = rootJsonObject.getJSONArray("result");
		if (resultArray.length() > 0) {
			JSONObject obj = resultArray.getJSONObject(0);
			PlaceInfo p = new PlaceInfo();
			p.setName(obj.getString("name"));
			JSONObject ll = obj.getJSONObject("location");
			p.setLat(ll.getDouble("lat"));
			p.setLng(ll.getDouble("lng"));
			exitList.add(p);
			// // System.out.println("D=>>"+p.toString());
		}

		if (exitList.size() < 3) {

			// e
			exitResult = HttpClientConnectionManager
					.httpGET(url1
							+ URLEncoder
									.encode(station_name + "地铁站e口", "UTF-8")
							+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("E=>>"+p.toString());
			}

			// f
			exitResult = HttpClientConnectionManager
					.httpGET(url1
							+ URLEncoder
									.encode(station_name + "地铁站f口", "UTF-8")
							+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("E=>>"+p.toString());
			}

			// g
			exitResult = HttpClientConnectionManager
					.httpGET(url1
							+ URLEncoder
									.encode(station_name + "地铁站g口", "UTF-8")
							+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("E=>>"+p.toString());
			}

			// h
			exitResult = HttpClientConnectionManager
					.httpGET(url1
							+ URLEncoder
									.encode(station_name + "地铁站h口", "UTF-8")
							+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("E=>>"+p.toString());
			}

			// j
			exitResult = HttpClientConnectionManager
					.httpGET(url1
							+ URLEncoder
									.encode(station_name + "地铁站j口", "UTF-8")
							+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("E=>>"+p.toString());
			}

		}

		if (exitList.size() < 3) {

			// a3
			exitResult = HttpClientConnectionManager.httpGET(url1
					+ URLEncoder.encode(station_name + "地铁站a3口", "UTF-8")
					+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("B=>>"+p.toString());
			}

			// a4
			exitResult = HttpClientConnectionManager.httpGET(url1
					+ URLEncoder.encode(station_name + "地铁站a4口", "UTF-8")
					+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("B=>>"+p.toString());
			}

			// b3
			exitResult = HttpClientConnectionManager.httpGET(url1
					+ URLEncoder.encode(station_name + "地铁站b3口", "UTF-8")
					+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("B=>>"+p.toString());
			}

			// b4
			exitResult = HttpClientConnectionManager.httpGET(url1
					+ URLEncoder.encode(station_name + "地铁站b4口", "UTF-8")
					+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("B=>>"+p.toString());
			}

			// c3
			exitResult = HttpClientConnectionManager.httpGET(url1
					+ URLEncoder.encode(station_name + "地铁站c3口", "UTF-8")
					+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("C=>>"+p.toString());
			}

			// c4
			exitResult = HttpClientConnectionManager.httpGET(url1
					+ URLEncoder.encode(station_name + "地铁站c4口", "UTF-8")
					+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("C=>>"+p.toString());
			}

			// d3
			exitResult = HttpClientConnectionManager.httpGET(url1
					+ URLEncoder.encode(station_name + "地铁站d3口", "UTF-8")
					+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("D=>>"+p.toString());
			}

			// d4
			exitResult = HttpClientConnectionManager.httpGET(url1
					+ URLEncoder.encode(station_name + "地铁站d4口", "UTF-8")
					+ url2);
			rootJsonObject = new JSONObject(exitResult);
			resultArray = rootJsonObject.getJSONArray("result");
			if (resultArray.length() > 0) {
				JSONObject obj = resultArray.getJSONObject(0);
				PlaceInfo p = new PlaceInfo();
				p.setName(obj.getString("name"));
				JSONObject ll = obj.getJSONObject("location");
				p.setLat(ll.getDouble("lat"));
				p.setLng(ll.getDouble("lng"));
				exitList.add(p);
				// // System.out.println("D=>>"+p.toString());
			}

		}

		// 如果没有找到相应的出站口
		if (exitList.size() == 0) {
			return null;
		}

		// Step2.2:计算距离最短的出站口
		double min = Double.MAX_VALUE;
		PlaceInfo placeMin = null;
		for (PlaceInfo p : exitList) {
			// System.out.println(p.toString());
			double dis = SubwayUtil.distaceOfTowPoints(p.getLat(), p.getLng(),
					placeInfo.getLat(), placeInfo.getLng());
			if (dis < min) {
				min = dis;
				placeMin = p;
			}
		}
		
		return placeMin;
		
	}
	
	/**
	 * 查询从A地去往B地的步行路线
	 * @param placeA
	 * @param placeB
	 * @throws JSONException 
	 * 
	 * @return 	walkString[0] 路线
	 * 			walkString[1] 距离
	 */
	public static String[] getWalkRoute(PlaceInfo placeA,PlaceInfo placeB) throws JSONException{
		
		String[] walkString = new String[2];

		String url = "http://api.map.baidu.com/direction/v1?mode=walking&origin="
				+ placeA.getName()
				+ "|"
				+ placeA.getLat()
				+ ","
				+ placeA.getLng()
				+ "&destination="
				+ placeB.getName()
				+ "|"
				+ placeB.getLat()
				+ ","
				+ placeB.getLng()
				+ "&origin_region=北京&destination_region=北京&output=json&ak="
				+ INFO.AK;
		StringBuffer buffer = new StringBuffer();
		String res = HttpClientConnectionManager.httpGET(url); 
		System.out.println("==>>"+res);
		JSONObject rootJsonObject = new JSONObject(res);
		String distance = "" + rootJsonObject.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getInt("distance");
		
		JSONArray stepsJsonArray = rootJsonObject.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getJSONArray("steps");
		for(int i=0;i<stepsJsonArray.length();i++){
			buffer.append(stepsJsonArray.getJSONObject(i).getString("instructions").replaceAll("<b>", " ").replaceAll("</b>", " "));
			buffer.append("#");
		}

		walkString[0] = buffer.toString();
		walkString[1] = distance;
		
		return walkString;
				
	}
	
	/**
	 * 查询换乘方案
	 * 考虑地铁到站时间和换站步行时间的最短时间换乘方案
	 * 
	 * 计算方法：
	 * 
	 * 1、获取用户输入起始、终点站名，并调用百度地图API获取两站的经纬度坐标；
	 * 2、百度接口查询换乘方案，得到换乘站点结果集transStationList；
	 * 3、遍历transStationList，结合换站步行时间表得到换乘方案:
	 * 		// 伪码：
	 * 		foreach(item In transStationList){
	 * 			
	 * 		}
	 * 
	 * @param startStationName
	 * @param endStationName
	 * @return
	 */
	public static ArrayList<String> queryTransMessage(String startStationName,String endStationName){

		ArrayList<String> list = new ArrayList<String>();
		PlaceInfo startStationPlaceInfo = getPlaceInfo(startStationName+"-地铁站");
		PlaceInfo endStationPlaceInfo = getPlaceInfo(endStationName+"-地铁站");
		
		String url1 = "http://api.map.baidu.com/direction/v2/transit?"
				+ "origin=" + startStationPlaceInfo.getLat()  + "," + startStationPlaceInfo.getLng() + 
				"&destination=" + endStationPlaceInfo.getLat() + "," + endStationPlaceInfo.getLng() + 
				"&tactics_incity=4&output=json&page_size=1"+"&ak="+INFO.AK;
		String transMethod = HttpClientConnectionManager.httpGET(url1);
		// System.out.println(transMethod);
		
		
		try {
			JSONObject rootJsonObject = new JSONObject(transMethod);
			JSONObject resultJsonObject = rootJsonObject.getJSONObject("result");
			JSONArray routesJsonArray = resultJsonObject.getJSONArray("routes");
			
			if(routesJsonArray.length() == 0){
				list.add("很抱歉，没有为您找到合适的换乘方案。");
				return list;
			}

			Date date = new Date();
			int currentHour = date.getHours();
			int currentMinu = date.getMinutes();
			String currentHourStr = "";
			String currentMinuStr = "";
			
			JSONArray stepsJsonArray = routesJsonArray.getJSONObject(0).getJSONArray("steps");
			for(int i=0;i<stepsJsonArray.length();i++){
				
				int distance = stepsJsonArray.getJSONArray(i).getJSONObject(0).getInt("distance");
				int duration = stepsJsonArray.getJSONArray(i).getJSONObject(0).getInt("duration") / 60;
				String instructions = stepsJsonArray.getJSONArray(i).getJSONObject(0).getString("instructions");
				
				// System.out.println("distance=>>"+distance);
				// System.out.println("duration=>>"+duration);
				// System.out.println("instructions=>>"+instructions);
				
				boolean onSubway = instructions.contains("乘地铁") || instructions.contains("方向"); // 该条数据是否是列车上
				String transString = "";
				
				// 结合列车时刻表换算时间
				if(onSubway){
					
					
					String temp_station_name = instructions.substring(0,instructions.indexOf("站"));
					String temp_subway_direction = instructions.substring(instructions.indexOf("线(")+2,instructions.indexOf("向)")+1);

					int old_currentHour = currentHour;
					int old_currentMinu = currentMinu;
					
					int[] nextSubwayTime = TimeTableUtil.formatTimeNext(temp_station_name, temp_subway_direction,currentHour, currentMinu);
					currentHour = nextSubwayTime[0];
					currentMinu = nextSubwayTime[1];
					
					if(currentHour == -1){
						int[] timePlus = TimeTableUtil.timePlus(old_currentHour, old_currentMinu, 3);
						currentHour = timePlus[0];
						currentMinu = timePlus[1];
						// transString += "   Error:lack of subway time-table data.";
						//return transString;
					}else if(currentHour == -2){
						int[] timePlus = TimeTableUtil.timePlus(old_currentHour, old_currentMinu, 3);
						currentHour = timePlus[0];
						currentMinu = timePlus[1];
						transString += " " + " 在" + instructions + "；";
						transString += "列车将停运，请选择其他出行方案。";
						//return transString;
					}
					
				}
				
				if (currentHour < 10) {
					currentHourStr = "0" + currentHour;
				}else{
					currentHourStr = "" + currentHour;
				}

				if (currentMinu < 10) {
					currentMinuStr = "0" + currentMinu;
				}else{
					currentMinuStr = "" + currentMinu;
				}
				
				if(onSubway){
					transString += currentHourStr + ":" + currentMinuStr + " 在" + instructions;
				}else{
					transString += currentHourStr + ":" + currentMinuStr + " " + instructions;
				}
				
				int[] timePlus = TimeTableUtil.timePlus(currentHour, currentMinu, duration);
				currentHour = timePlus[0];
				currentMinu = timePlus[1];
				
				if (currentHour < 10) {
					currentHourStr = "0" + currentHour;
				}else{
					currentHourStr = "" + currentHour;
				}

				if (currentMinu < 10) {
					currentMinuStr = "0" + currentMinu;
				}else{
					currentMinuStr = "" + currentMinu;
				}

				transString += "；预计耗时" + duration + "分钟；" + currentHourStr + ":" + currentMinuStr + "到达。";

				list.add(transString);
				
				
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		return list;
		
	}

	
	
	public static void main(String[] args){
		
		// System.out.println(getPlaceInfo("北京理工大学"));
		//quaryExitInfo("北京理工大学","魏公村站");
	}

}
