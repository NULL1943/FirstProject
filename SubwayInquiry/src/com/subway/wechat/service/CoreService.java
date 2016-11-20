package com.subway.wechat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.subway.util.GetUserInfoUtil;
import com.subway.util.MessageUtil;
import com.subway.wechat.entity.resp.Article;
import com.subway.wechat.entity.resp.NewsMessage;
import com.subway.wechat.entity.resp.TextMessage;


/**
 * ���ķ�����
 */
public class CoreService {
	
	/**
	 * ����΢�ŷ���������
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// Ĭ�Ϸ��ص��ı���Ϣ����
			String respContent = "�������쳣�����Ժ��ԣ�";
			// ������Ϣ������MessageUtil����΢�ŷ�����xml��ʽ����Ϣ�������Ľ������HashMap��
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// ��HashMap��ȡ����Ϣ�еĹ����ֶ�:���ͷ��ʺţ�open_id��
			String fromUserName = requestMap.get("FromUserName");
			// ��HashMap��ȡ����Ϣ�еĹ����ֶ�:�����ʺ�
			String toUserName = requestMap.get("ToUserName");
			// ��HashMap��ȡ����Ϣ�еĹ����ֶ�:��Ϣ����
			String msgType = requestMap.get("MsgType");
			
			/* ����΢�ŷ��͵ĸ����͵���Ϣ������MsgType�ж������������͵���Ϣ������Ӧ����Ϣ���Է��� */
			/**
			 * �ı���Ϣ
			 */
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

				// ��װҪ���ص��ı���Ϣ����:�ظ��ı���Ϣ
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag(0);

				// ������ı���Ϣ���ٵõ�����content
				String content = requestMap.get("Content");

				if ("?".equals(content) || "��".equals(content)) {
					respContent = MessageUtil.getMainMenu();
				} else if ("1".equals(content)) {
					respContent = MessageUtil.get2JiResponseMessage();
				} else if ("2".equals(content)) {
					respContent = MessageUtil.getBaoXiuResponseMessage();
				} else if ("3".equals(content)) {
					respContent = MessageUtil.getWaiLianResponseMessage();
				} else if ("4".equals(content)) {
					respContent = MessageUtil.getLianChuangResponseMessage();
				} else if ("5".equals(content)) {
					respContent = MessageUtil.getBusResponseMessage();
				} else if ("6".equals(content)) {
					respContent = MessageUtil.getFaceResponseMessage();
				} else if ("7".equals(content)) {
					respContent = MessageUtil.getChattingResponseMessage();
				} else if ("����".equals(content)) {
					// ������Ϣ��requestMap.get("Content");

					// ������Ϣ
					respContent = MessageUtil
							.getNormalLianChuangResponseMessage();
				} else if ("����".equals(content)) {
					// ������Ϣ��requestMap.get("Content");

					respContent = MessageUtil.getNormalBaoXiuResponseMessage();
				} else if ("����".equals(content)) {
					// ������Ϣ��requestMap.get("Content");

					respContent = MessageUtil.getNormal2JiResponseMessage();
				} else if ("����".equals(content)) {
					// ������Ϣ��requestMap.get("Content");

					// ������Ϣ
					respContent = MessageUtil.getNormalWaiLianResponseMessage();
				} 
				
			
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			/**
			 * ͼƬ��Ϣ������ʶ��
			 */
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				
				// ��ȡͼƬurl
				String picUrl = requestMap.get("PicUrl");
				
				
				
			}
			/**
			 * ����λ����Ϣ
			 */
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag(0);
				respContent = "�����͵��ǵ���λ����Ϣ��";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			/**
			 * ������Ϣ
			 */
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag(0);
				respContent = "�����͵���������Ϣ��";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			/**
			 * ��Ƶ��Ϣ
			 */
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag(0);
				respContent = "�����͵�����Ƶ��Ϣ��";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			
			/**
			 * �¼����ͣ�MsgType=eventʱ���ͱ�ʾ����һ���¼�������Ϣ
			 */
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// �¼�����
				String eventType = requestMap.get("Event");
				// ��ע
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					TextMessage textMessage = new TextMessage();
					textMessage.setToUserName(fromUserName);
					textMessage.setFromUserName(toUserName);
					textMessage.setCreateTime(new Date().getTime());
					textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					textMessage.setFuncFlag(0);
					respContent = MessageUtil.getWelcomes();
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
					
					GetUserInfoUtil.StoreUser(fromUserName);
				}
				// ȡ����ע
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO ȡ�����ĺ��û����ղ������ںŷ��͵���Ϣ����˲���Ҫ�ظ���Ϣ
				}
				
				// �Զ���˵�����¼�
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// �¼�KEYֵ���봴���Զ���˵�ʱָ����KEYֵ��Ӧ
					String eventKey = requestMap.get("EventKey");
					
					// ��Ϸ
					if(eventKey.equals("fuwu_youxi")){
						// ����ͼ����Ϣ   
			            NewsMessage newsMessage = new NewsMessage();  
			            newsMessage.setToUserName(fromUserName);  
			            newsMessage.setFromUserName(toUserName);  
			            newsMessage.setCreateTime(new Date().getTime());  
			            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
			            newsMessage.setFuncFlag(0);  
			            
						List<Article> articleList = new ArrayList<Article>(); 
						
						Article article = new Article();  
	                    article.setTitle("A01-�����ϳ���������������Ů��");  
	                    article.setDescription("�����������ò��������Ũü�����ۡ��߱ǣ��׳���Ů�������ѧ������Ů�������������������ʶ�����������������Ů��");  
	                    article.setPicUrl("http://mucjsj.duapp.com/wechat/pictures/1.jpg");  
	                    article.setUrl("http://mucjsj.duapp.com/wechat/game/mm/index.html");
	                    articleList.add(article);  
	                    // ����ͼ����Ϣ����   
	                    newsMessage.setArticleCount(articleList.size());  
	                    // ����ͼ����Ϣ������ͼ�ļ���   
	                    newsMessage.setArticles(articleList);  
	                    // ��ͼ����Ϣ����ת����xml�ַ���   
	                    respMessage = MessageUtil.newsMessageToXml(newsMessage); 
					}
					// �
					else if(eventKey.equals("jixie_huodong")){
						// ����ͼ����Ϣ   
			            NewsMessage newsMessage = new NewsMessage();  
			            newsMessage.setToUserName(fromUserName);  
			            newsMessage.setFromUserName(toUserName);  
			            newsMessage.setCreateTime(new Date().getTime());  
			            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
			            newsMessage.setFuncFlag(0);  
			            
						List<Article> articleList = new ArrayList<Article>(); 
						
						Article article = new Article();  
	                    article.setTitle("��ӭ�������������ѧ�����Э�ᣡ");  
	                    article.setDescription("���������ѧ�����Э�ᣬ�����ڷ���ȫУʦ������Ϣ����������Э�Ὣ�������������ƣ��Լ����֪ʶ��ѵΪ ��������Χ��IT��ҵ��չ��ɫ�Ϊ���ģ���ȡ�����ɹ����鲢�ܽ᲻��֮�� �����Ժ�Ĺ�����һ��һ����ӡ��ʵ������ȥ�������� ��У���Ź����ķ�չ��ͼ��д���Լ�Ũī�زʵ�һ�ʡ�");  
	                    article.setPicUrl("http://mucjsj.duapp.com/wechat/pictures/2.jpg");  
	                    article.setUrl("http://mucjsj.duapp.com/wechat/ready/index.html");
	                    articleList.add(article);
	                    // ����ͼ����Ϣ����
	                    newsMessage.setArticleCount(articleList.size());  
	                    // ����ͼ����Ϣ������ͼ�ļ���   
	                    newsMessage.setArticles(articleList);  
	                    // ��ͼ����Ϣ����ת����xml�ַ���   
	                    respMessage = MessageUtil.newsMessageToXml(newsMessage); 
					}
					// С֪ʶ
					else if(eventKey.equals("fuwu_zhishi")){
						
						// ����ͼ����Ϣ   
			            NewsMessage newsMessage = new NewsMessage();  
			            newsMessage.setToUserName(fromUserName);  
			            newsMessage.setFromUserName(toUserName);  
			            newsMessage.setCreateTime(new Date().getTime());  
			            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
			            newsMessage.setFuncFlag(0);  
			            
						List<Article> articleList = new ArrayList<Article>(); 
						
						Article article = new Article();  
	                    article.setTitle("���������֪ʶ�ռ�");  
	                    article.setDescription("�ڵ��£��������Ϊһ�����ɻ�ȱ�Ĺ��ߣ�������ѧϰ�����������������У����벻������Ϊ���ù��ʦ���˽��������úü��������һ���ռ����������֪ʶ�����ǽ�½�����������һϵ�м��������֪ʶ�����£����Ҽ�ʱ��ע��");  
	                    article.setPicUrl("http://mucjsj.duapp.com/wechat/pictures/3.jpg");  
	                    article.setUrl("http://mucjsj.duapp.com/wechat/ready/index.html");
	                    articleList.add(article);  
	                    // ����ͼ����Ϣ����   
	                    newsMessage.setArticleCount(articleList.size());  
	                    // ����ͼ����Ϣ������ͼ�ļ���   
	                    newsMessage.setArticles(articleList);  
	                    // ��ͼ����Ϣ����ת����xml�ַ���   
	                    respMessage = MessageUtil.newsMessageToXml(newsMessage); 
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessage;
	}

}