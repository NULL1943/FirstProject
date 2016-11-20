package com.subway.wechat.menu;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.subway.http.HttpClientConnectionManager;
import com.subway.util.GetAccessToken;
import com.subway.util.INFO;

public class MenuUtils {

	// http客户端
	public static DefaultHttpClient httpclient;

	static {
		httpclient = new DefaultHttpClient();
		httpclient = (DefaultHttpClient) HttpClientConnectionManager
				.getSSLInstance(httpclient);
		// 接受任何证书的浏览器客户端
	}

	public static void main(String[] args) throws Exception {
		/*{\"button\":[{\"name\":\"计算机协会\",\"sub_button\":[{	\"type\":\"view\",\"name\":\"官网\",\"url\":\"http://mucjsj.duapp.com\"},{\"type\":\"view\",\"name\":\"二级\",\"url\":\"http://mucjsj.duapp.com\"},{\"type\":\"view\",\"name\":\"报修\",\"url\":\"http://mucjsj.duapp.com\"},{\"type\":\"click\",\"name\":\"活动\",\"key\":\"jixie_huodong\"}]},{\"name\":\"知民大乎\",\"sub_button\":[{\"type\":\"view\",\"name\":\"知民大乎\",\"url\":\"http://mucjsj.duapp.com\"},{\"type\":\"view\",\"name\":\"所见[测试版]\",\"url\":\"http://mucjsj.duapp.com/wechat/cos/index.html\"}]},{\"name\":\"服务\",\"sub_button\":[{\"type\":\"view\",\"name\":\"校园网[内网访问]\",\"url\":\"http://10.10.167.100:8080/CampusNetwork/index.html\"},{\"type\":\"pic_photo_or_album\",\"name\":\"人脸识别\",\"key\":\"fuwu_renlian\"},{\"type\":\"click\",\"name\":\"游戏\",\"key\":\"fuwu_youxi\"}]}]}*/		
		
		
		// String menu = "{\"button\":[{\"name\":\"计算机协会\",\"sub_button\":[{\"type\":\"view\",\"name\":\"官网\",\"url\":\"http://mucjsj.duapp.com/wechat/club/index.html\"},{\"type\":\"view\",\"name\":\"知民大乎\",\"url\":\"http://mucjsj.duapp.com/wechat/ready/index.html\"},{\"type\":\"click\",\"name\":\"活动\",\"key\":\"jixie_huodong\"}]},{\"name\":\"故障报修\",\"sub_button\":[{\"type\":\"view\",\"name\":\"我要求助\",\"url\":\"http://x.languang.com/weixin/mem.MemberMaintainer.memberHelp.hf\"},{\"type\":\"view\",\"name\":\"维修记录\",\"url\":\"http://x.languang.com/weixin/mem.MemberOrderList.memberOrder.hf\"},{\"type\":\"view\",\"name\":\"个人信息\",\"url\":\"http://x.languang.com/weixin/mem.MemberOrderList.memberInformationList.hf\"},{\"type\":\"view\",\"name\":\"维修技巧\",\"url\":\"http://x.languang.com/weixin/mem.MemberOrderList.historyInformationList.hf\"},{\"type\":\"view\",\"name\":\"大咖对话\",\"url\":\"http://x.languang.com/weixin/chat.ChatRoom.index.hf\"}]},{\"name\":\"服务\",\"sub_button\":[{\"type\":\"view\",\"name\":\"校园网认证[内网访问]\",\"url\":\"http://10.10.167.100:8080/CampusNetwork/index.html\"},{\"type\":\"click\",\"name\":\"基础知识\",\"key\":\"fuwu_zhishi\"},{\"type\":\"pic_photo_or_album\",\"name\":\"人脸识别\",\"key\":\"fuwu_renlian\"},{\"type\":\"click\",\"name\":\"游戏民大\",\"key\":\"fuwu_youxi\"}]}]}";
		String menu = "{\"button\":[{\"type\":\"view\",\"name\":\"H E R E\",\"url\":\"http://muchere.duapp.com/\"}]}";
		String at = GetAccessToken.getAccessToken(httpclient,INFO.AppID,INFO.AppSecret);
		System.out.println(createMenu(menu, at));
		//System.out.println(deleteMenu(at));
		System.out.println(getMenuInfo(at));
	}

	/**
	 * 创建菜单
	 * 
	 * @param params
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public static String createMenu(String params, String accessToken)
			throws Exception {
		HttpPost httpost = HttpClientConnectionManager
				.getPostMethod("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="
						+ accessToken);
		httpost.setEntity(new StringEntity(params, "UTF-8"));
		HttpResponse response = httpclient.execute(httpost);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		System.out.println(jsonStr);

		JSONObject object = JSON.parseObject(jsonStr);
		return object.getString("errmsg");

	}	

	/**
	 * 删除自定义菜单
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public static String deleteMenu(String accessToken) throws Exception {
		HttpGet get = HttpClientConnectionManager
				.getGetMethod("https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="
						+ accessToken);
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object = JSON.parseObject(jsonStr);
		return object.getString("errmsg");
	}
	
	 /**
	  * 查询菜单
	  * @param accessToken
	  * @return
	  * @throws Exception
	  */
	 public static String getMenuInfo(String accessToken) throws Exception {
	  HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + accessToken);
	  HttpResponse response = httpclient.execute(get);
	  String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
	  return jsonStr;
	 }

}
