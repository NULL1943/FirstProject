package com.subway.wechat.entity.req;

/**
 * ������Ϣʵ����ࣨ��ͨ�û�->�����˺ţ�
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
