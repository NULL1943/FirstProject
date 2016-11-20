package com.subway.wechat.entity.req;

/**
 * 请求消息实体基类（普通用户->公众账号）
 * @author NULL
 *
 */
public class BaseMessage {
	

	private long CreateTime;
	private String MsgType;
	private long MessageId;
	
	public long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public long getMessageId() {
		return MessageId;
	}
	public void setMessageId(long messageId) {
		MessageId = messageId;
	}
	
	
	
}
