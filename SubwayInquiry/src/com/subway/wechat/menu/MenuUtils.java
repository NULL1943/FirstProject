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

	// http�ͻ���
	public static DefaultHttpClient httpclient;

	static {
		httpclient = new DefaultHttpClient();
		httpclient = (DefaultHttpClient) HttpClientConnectionManager
				.getSSLInstance(httpclient);
		// �����κ�֤���������ͻ���
	}

	public static void main(String[] args) throws Exception {
		/*{\"button\":[{\"name\":\"�����Э��\",\"sub_button\":[{	\"type\":\"view\",\"name\":\"����\",\"url\":\"http://mucjsj.duapp.com\"},{\"type\":\"view\",\"name\":\"����\",\"url\":\"http://mucjsj.duapp.com\"},{\"type\":\"view\",\"name\":\"����\",\"url\":\"http://mucjsj.duapp.com\"},{\"type\":\"click\",\"name\":\"�\",\"key\":\"jixie_huodong\"}]},{\"name\":\"֪����\",\"sub_button\":[{\"type\":\"view\",\"name\":\"֪����\",\"url\":\"http://mucjsj.duapp.com\"},{\"type\":\"view\",\"name\":\"����[���԰�]\",\"url\":\"http://mucjsj.duapp.com/wechat/cos/index.html\"}]},{\"name\":\"����\",\"sub_button\":[{\"type\":\"view\",\"name\":\"У԰��[��������]\",\"url\":\"http://10.10.167.100:8080/CampusNetwork/index.html\"},{\"type\":\"pic_photo_or_album\",\"name\":\"����ʶ��\",\"key\":\"fuwu_renlian\"},{\"type\":\"click\",\"name\":\"��Ϸ\",\"key\":\"fuwu_youxi\"}]}]}*/		
		
		
		// String menu = "{\"button\":[{\"name\":\"�����Э��\",\"sub_button\":[{\"type\":\"view\",\"name\":\"����\",\"url\":\"http://mucjsj.duapp.com/wechat/club/index.html\"},{\"type\":\"view\",\"name\":\"֪����\",\"url\":\"http://mucjsj.duapp.com/wechat/ready/index.html\"},{\"type\":\"click\",\"name\":\"�\",\"key\":\"jixie_huodong\"}]},{\"name\":\"���ϱ���\",\"sub_button\":[{\"type\":\"view\",\"name\":\"��Ҫ����\",\"url\":\"http://x.languang.com/weixin/mem.MemberMaintainer.memberHelp.hf\"},{\"type\":\"view\",\"name\":\"ά�޼�¼\",\"url\":\"http://x.languang.com/weixin/mem.MemberOrderList.memberOrder.hf\"},{\"type\":\"view\",\"name\":\"������Ϣ\",\"url\":\"http://x.languang.com/weixin/mem.MemberOrderList.memberInformationList.hf\"},{\"type\":\"view\",\"name\":\"ά�޼���\",\"url\":\"http://x.languang.com/weixin/mem.MemberOrderList.historyInformationList.hf\"},{\"type\":\"view\",\"name\":\"�󿧶Ի�\",\"url\":\"http://x.languang.com/weixin/chat.ChatRoom.index.hf\"}]},{\"name\":\"����\",\"sub_button\":[{\"type\":\"view\",\"name\":\"У԰����֤[��������]\",\"url\":\"http://10.10.167.100:8080/CampusNetwork/index.html\"},{\"type\":\"click\",\"name\":\"����֪ʶ\",\"key\":\"fuwu_zhishi\"},{\"type\":\"pic_photo_or_album\",\"name\":\"����ʶ��\",\"key\":\"fuwu_renlian\"},{\"type\":\"click\",\"name\":\"��Ϸ���\",\"key\":\"fuwu_youxi\"}]}]}";
		String menu = "{\"button\":[{\"type\":\"view\",\"name\":\"H E R E\",\"url\":\"http://muchere.duapp.com/\"}]}";
		String at = GetAccessToken.getAccessToken(httpclient,INFO.AppID,INFO.AppSecret);
		System.out.println(createMenu(menu, at));
		//System.out.println(deleteMenu(at));
		System.out.println(getMenuInfo(at));
	}

	/**
	 * �����˵�
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
	 * ɾ���Զ���˵�
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
	  * ��ѯ�˵�
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
