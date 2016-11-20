package com.subway.wechat.entity.resp;

/**
 * 文本消息
 * @author NULL
 *
 */
public class TextMessage extends BaseMessage {
	
	/**
	 *  回复的消息内容
	 */
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
