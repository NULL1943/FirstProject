package com.subway.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.subway.dao.StoreUserDao;
import com.subway.http.HttpClientConnectionManager;

/**
 * ���ں���δ��ô�Ȩ��
 * ��ȡ�û�������Ϣ 
 * �ο���http://www.cnblogs.com/txw1958/p/weixin76-user-info.html
 * URL:https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID
 * @author NULL
 *
 */
public class GetUserInfoUtil {

	public GetUserInfoUtil() {
	}

	// http�ͻ���
	public static DefaultHttpClient httpclient;
	static {
		httpclient = new DefaultHttpClient();
		httpclient = (DefaultHttpClient) HttpClientConnectionManager
				.getSSLInstance(httpclient);
		// �����κ�֤���������ͻ���
	}

	/**
	 * ��ȡ�û���Ϣ
	 * @param openid
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void getUserInfo(String openid) throws ClientProtocolException, IOException {
		String at = GetAccessToken.getAccessToken(httpclient, INFO.AppID, INFO.AppSecret);
		String api = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+at+"&openid="+openid;
		HttpPost http = HttpClientConnectionManager.getPostMethod(api);
		HttpResponse response = httpclient.execute(http);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		System.out.println(jsonStr);
	}
	
	/**
	 * �洢�û���openid
	 * @param openid
	 */
	public static void StoreUser(String openid){
		StoreUserDao dao = new StoreUserDao();
		dao.storeAUser(openid);
	}
}
