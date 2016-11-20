package com.subway.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.subway.http.HttpClientConnectionManager;

public class GetAccessToken {

	public GetAccessToken() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * ªÒ»°AccessToken
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String getAccessToken(DefaultHttpClient httpclient,String appid, String secret)
			throws ClientProtocolException, IOException {
		HttpGet get = HttpClientConnectionManager
				.getGetMethod("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
						+ appid + "&secret=" + secret);
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object = JSON.parseObject(jsonStr);
		return object.getString("access_token");
	}
}
