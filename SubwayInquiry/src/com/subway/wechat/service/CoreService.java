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
 * 核心服务类
 */
public class CoreService {
	
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";
			// 调用消息工具类MessageUtil解析微信发来的xml格式的消息，解析的结果放在HashMap里
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 从HashMap中取出消息中的公有字段:发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 从HashMap中取出消息中的公有字段:公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 从HashMap中取出消息中的公有字段:消息类型
			String msgType = requestMap.get("MsgType");
			
			/* 接收微信发送的各类型的消息，根据MsgType判断属于哪种类型的消息并对相应的消息予以返回 */
			/**
			 * 文本消息
			 */
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

				// 组装要返回的文本消息对象:回复文本消息
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag(0);

				// 如果是文本消息，再得到它的content
				String content = requestMap.get("Content");

				if ("?".equals(content) || "？".equals(content)) {
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
				} else if ("联创".equals(content)) {
					// 处理消息：requestMap.get("Content");

					// 反馈消息
					respContent = MessageUtil
							.getNormalLianChuangResponseMessage();
				} else if ("报修".equals(content)) {
					// 处理消息：requestMap.get("Content");

					respContent = MessageUtil.getNormalBaoXiuResponseMessage();
				} else if ("二级".equals(content)) {
					// 处理消息：requestMap.get("Content");

					respContent = MessageUtil.getNormal2JiResponseMessage();
				} else if ("外联".equals(content)) {
					// 处理消息：requestMap.get("Content");

					// 反馈消息
					respContent = MessageUtil.getNormalWaiLianResponseMessage();
				} 
				
			
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			/**
			 * 图片消息：人脸识别
			 */
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				
				// 获取图片url
				String picUrl = requestMap.get("PicUrl");
				
				
				
			}
			/**
			 * 地理位置消息
			 */
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag(0);
				respContent = "您发送的是地理位置消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			/**
			 * 链接消息
			 */
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag(0);
				respContent = "您发送的是链接消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			/**
			 * 音频消息
			 */
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag(0);
				respContent = "您发送的是音频消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			
			/**
			 * 事件推送：MsgType=event时，就表示这是一条事件推送消息
			 */
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 关注
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
				// 取消关注
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");
					
					// 游戏
					if(eventKey.equals("fuwu_youxi")){
						// 创建图文消息   
			            NewsMessage newsMessage = new NewsMessage();  
			            newsMessage.setToUserName(fromUserName);  
			            newsMessage.setFromUserName(toUserName);  
			            newsMessage.setCreateTime(new Date().getTime());  
			            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
			            newsMessage.setFuncFlag(0);  
			            
						List<Article> articleList = new ArrayList<Article>(); 
						
						Article article = new Article();  
	                    article.setTitle("A01-你能认出几个少数民族美女？");  
	                    article.setDescription("少数民族的相貌多数都是浓眉、大眼、高鼻，易出美女。民族大学更是美女如云令人如痴如醉。你能识别出几个少数民族美女？");  
	                    article.setPicUrl("http://mucjsj.duapp.com/wechat/pictures/1.jpg");  
	                    article.setUrl("http://mucjsj.duapp.com/wechat/game/mm/index.html");
	                    articleList.add(article);  
	                    // 设置图文消息个数   
	                    newsMessage.setArticleCount(articleList.size());  
	                    // 设置图文消息包含的图文集合   
	                    newsMessage.setArticles(articleList);  
	                    // 将图文消息对象转换成xml字符串   
	                    respMessage = MessageUtil.newsMessageToXml(newsMessage); 
					}
					// 活动
					else if(eventKey.equals("jixie_huodong")){
						// 创建图文消息   
			            NewsMessage newsMessage = new NewsMessage();  
			            newsMessage.setToUserName(fromUserName);  
			            newsMessage.setFromUserName(toUserName);  
			            newsMessage.setCreateTime(new Date().getTime());  
			            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
			            newsMessage.setFuncFlag(0);  
			            
						List<Article> articleList = new ArrayList<Article>(); 
						
						Article article = new Article();  
	                    article.setTitle("欢迎加入中央民族大学计算机协会！");  
	                    article.setDescription("中央民族大学计算机协会，致力于服务全校师生的信息生活。今后计算机协会将立足于自身优势，以计算机知识培训为 基础、以围绕IT产业开展特色活动为中心，汲取以往成功经验并总结不足之处 ，在以后的工作中一步一个脚印坚实地走下去，力争在 我校社团工作的发展蓝图上写下自己浓墨重彩的一笔。");  
	                    article.setPicUrl("http://mucjsj.duapp.com/wechat/pictures/2.jpg");  
	                    article.setUrl("http://mucjsj.duapp.com/wechat/ready/index.html");
	                    articleList.add(article);
	                    // 设置图文消息个数
	                    newsMessage.setArticleCount(articleList.size());  
	                    // 设置图文消息包含的图文集合   
	                    newsMessage.setArticles(articleList);  
	                    // 将图文消息对象转换成xml字符串   
	                    respMessage = MessageUtil.newsMessageToXml(newsMessage); 
					}
					// 小知识
					else if(eventKey.equals("fuwu_zhishi")){
						
						// 创建图文消息   
			            NewsMessage newsMessage = new NewsMessage();  
			            newsMessage.setToUserName(fromUserName);  
			            newsMessage.setFromUserName(toUserName);  
			            newsMessage.setCreateTime(new Date().getTime());  
			            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
			            newsMessage.setFuncFlag(0);  
			            
						List<Article> articleList = new ArrayList<Article>(); 
						
						Article article = new Article();  
	                    article.setTitle("计算机基础知识普及");  
	                    article.setDescription("在当下，计算机成为一个不可或缺的工具，无论是学习、工作，还是生活中，都离不开它。为了让广大师生了解计算机、用好计算机，进一步普及计算机基础知识。我们将陆续整理和推送一系列计算机基础知识的文章，请大家及时关注。");  
	                    article.setPicUrl("http://mucjsj.duapp.com/wechat/pictures/3.jpg");  
	                    article.setUrl("http://mucjsj.duapp.com/wechat/ready/index.html");
	                    articleList.add(article);  
	                    // 设置图文消息个数   
	                    newsMessage.setArticleCount(articleList.size());  
	                    // 设置图文消息包含的图文集合   
	                    newsMessage.setArticles(articleList);  
	                    // 将图文消息对象转换成xml字符串   
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