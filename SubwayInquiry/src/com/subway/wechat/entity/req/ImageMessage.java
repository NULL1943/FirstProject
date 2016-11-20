package com.subway.wechat.entity.req;

/**
 * 图片消息实体
 * @author NULL
 *
 */
public class ImageMessage extends BaseMessage {
	
	/**
	 * 图片链接
	 */
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	
}
