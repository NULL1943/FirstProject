package com.subway.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.subway.wechat.entity.resp.Article;
import com.subway.wechat.entity.resp.MusicMessage;
import com.subway.wechat.entity.resp.NewsMessage;
import com.subway.wechat.entity.resp.TextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 消息工具类
 */
public class MessageUtil {

	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	/**
	 * 解析微信发来的请求（XML）
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(inputStream);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		try {
			inputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		inputStream = null;

		return map;
	}

	/**
	 * 文本消息对象转换成xml
	 *
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 音乐消息对象转换成xml
	 *
	 * @param musicMessage
	 *            音乐消息对象
	 * @return xml
	 */
	public static String musicMessageToXml(MusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	/**
	 * 图文消息对象转换成xml
	 *
	 * @param newsMessage
	 *            图文消息对象
	 * @return xml
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 *
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@SuppressWarnings("unchecked")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});

	/**
	 * 判断是否是QQ表情
	 *
	 * @param content
	 * @return
	 */
	public static boolean isQqFace(String content) {
		boolean result = false;

		// 判断QQ表情的正则表达式
		String qqfaceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
		Pattern p = Pattern.compile(qqfaceRegex);
		Matcher m = p.matcher(content);
		if (m.matches()) {
			result = true;
		}
		return result;
	}

	/**
	 * emoji表情转换(hex -> utf-16)
	 *
	 * @param hexEmoji
	 * @return
	 */
	public static String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}

	public static String getWelcomes() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("像你这样识货的人很少见了/:B-)").append("\n\n");
		buffer.append("欢迎关注小乎，很高兴为您服务！").append("\n\n");
		buffer.append(
				"我们致力于推广普及计算机基础知识，汇聚和培养更多计算机爱好者，以提高会员计算机水平为己任，并有培养技术精英的网络工作室，为繁荣校园文化做贡献。")
				.append("\n\n");
		buffer.append("回复“?”显示此帮助菜单").append("\n\n");
		buffer.append("回复其他内容可以和小乎机器人聊天哦~");
		return buffer.toString();
	}

	/**
	 * 主菜单
	 *
	 * @return
	 */
	public static String getMainMenu() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("您好，请回复数字选择服务：").append("\n\n");
		/*buffer.append("1  计算机二级").append("\n");
		buffer.append("2  电脑故障报修").append("\n");
		buffer.append("3  外联洽谈").append("\n");
		buffer.append("4  联创网络工作室").append("\n");
		buffer.append("5  公交查询").append("\n");
		buffer.append("6  人脸识别").append("\n");
		buffer.append("7  聊天唠嗑").append("\n\n");*/
		buffer.append("1  计算机二级").append("\n");
		buffer.append("2  电脑故障报修").append("\n");

		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

	/**
	 * 计算机二级 接收回复："二级：...."
	 * 
	 * @return
	 */
	public static String get2JiResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F449) + " 计算机二级").append("\n\n");

		buffer.append("我们为同学们提供了计算机二级的培训活动，按照如下格式回复可报名参加：").append("\n");
		buffer.append("二级：").append("\n");
		buffer.append("+ 你的信息+联系方式").append("\n\n");

		buffer.append("更多信息请您关注：").append("\n");
		;
		buffer.append("<a href='http://mucjsj.duapp.com/'>中央民族大学计算机协会</a>")
				.append("\n\n");

		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

	/**
	 * 电脑故障报修 接收回复："报修：...."
	 * 
	 * @return
	 */
	public static String getBaoXiuResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F528) + " 电脑故障报修").append("\n\n");
/*
		buffer.append("请按照如下格式对您的电脑故障进行描述：").append("\n");
		buffer.append("报修：").append("\n");
		buffer.append("+ 你的信息+联系方式").append("\n");
		buffer.append("+ 你的电脑故障描述（请尽量详细）").append("\n\n");

		buffer.append("我们将在官网上发布我们的处理结果，请您关注：").append("\n");
		;
		buffer.append("<a href='http://mucjsj.duapp.com/'>中央民族大学计算机协会</a>")
				.append("\n\n");
*/
		buffer.append("请在下方菜单栏中选择“故障报修”模块进行故障报修\n如果无法注册，请先关注【蓝光校园联盟】公众平台，微信号：lgxylm，在此公众号中选择报修并注册账号，此后即可在此公众号中享受故障报修服务。").append("\n\n");
		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

	/**
	 * 外联洽谈 接收回复："外联：...."
	 * 
	 * @return
	 */
	public static String getWaiLianResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("/:share 外联洽谈").append("\n\n");

		buffer.append(
				"计算机协会与各界人士、组织长期保持良好的合作关系，如需洽谈合作，请按照下面的格式回复，我们将在最快时间给您回复。您也可以给我们发邮件：mucjsj@163.com")
				.append("\n\n");

		buffer.append("外联：").append("\n");
		buffer.append("+ 你的信息和联系方式").append("\n");
		buffer.append("+ 你要发送的内容").append("\n\n");

		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

	/**
	 * 联创网络工作室 接收回复："联创：...."
	 * 
	 * @return
	 */
	public static String getLianChuangResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F4BB) + " 联创网络工作室简介").append("\n\n");

		buffer.append(
				"联创网络工作室是计算机协会下属的、我校对计算机感兴趣的精英集结地, 以现代教育技术部为依托、以信息工程学院为主要科技阵地的学生自主参与、组织、管理的致力于研究和发展计算机技术、互联网创业的科技型、实践型的学生组织。吸纳并培养一批具有较高计算机技术水平的学生精英。")
				.append("\n\n");

		buffer.append(
				"如果你对计算机技术有浓厚的兴趣，就请加入我们吧！不论专业、年级，我们都希望优秀的你加入！ " + emoji(0x2764))
				.append("\n");
		buffer.append("按照如下格式回复你的信息或者其他你想说的话：").append("\n\n");

		buffer.append("联创：").append("\n");
		buffer.append("+ 你要发送的内容").append("\n\n");

		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

	/**
	 * 公交查询功能介绍信息
	 * 
	 * @return
	 */
	public static String getBusResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("公交查询使用指南").append("\n\n");

		buffer.append(emoji(0x1F31F) + " 查询城市公交线路").append("\n");
		buffer.append("格式：城市，线路名称").append("\n");
		buffer.append("例如：贵阳，2路").append("\n\n");

		buffer.append(emoji(0x1F31F) + " 查询城市公交驾乘方案").append("\n");
		buffer.append("格式：城市，起点至终点").append("\n");
		buffer.append("例如：北京，魏公村至天安门").append("\n\n");

		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

	/**
	 * 人脸识别功能介绍信息
	 * 
	 * @return
	 */
	public static String getFaceResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F466) + " 人脸识别使用指南").append("\n\n");

		buffer.append("发送一张清晰的照片，小q就能帮你分析出种族、年龄、性别等信息哦").append("\n");
		buffer.append("快来试试你是不是长得太着急 " + emoji(0x1F383)).append("\n\n");

		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

	/**
	 * 聊天唠嗑功能介绍信息
	 * 
	 * @return
	 */
	public static String getChattingResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F61C) + " 聊天唠嗑使用说明").append("\n\n");

		buffer.append("闲暇无聊，来唠嗑吧，有问必答！例如：").append("\n");
		buffer.append(emoji(0x1F449) + " 讲个笑话 ").append("\n");
		buffer.append(emoji(0x1F449) + " 新疆有什么好玩的 ").append("\n");
		buffer.append(emoji(0x1F449) + " 贵阳的区号 ").append("\n");
		buffer.append(emoji(0x1F449) + " 订票电话").append("\n\n");

		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

	/**
	 * 反馈回执（联创）
	 * 
	 * @return
	 */
	public static String getNormalLianChuangResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F514) + "  反馈回执").append("\n\n");

		buffer.append("您的消息我们已收到，我们会尽快对您的消息进行处理，请及时关注联创网络工作室的动态。").append(
				"\n\n");

		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

	/**
	 * 反馈回执（外联）
	 * 
	 * @return
	 */
	public static String getNormalWaiLianResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F514) + "  反馈回执").append("\n\n");

		buffer.append("您的消息我们已收到，我们会尽快对您的消息进行处理，请及时关注计算机协会的动态。").append("\n\n");

		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

	/**
	 * 反馈回执（报修）
	 * 
	 * @return
	 */
	public static String getNormalBaoXiuResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F514) + "  反馈回执").append("\n\n");

		buffer.append("您的报修消息我们已收到，我们将在官网上发布我们的处理结果，请您关注：").append("\n");
		buffer.append("<a href='http://mucjsj.duapp.com/'>中央民族大学计算机协会</a>")
				.append("\n\n\n");

		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

	/**
	 * 反馈回执（二级）
	 * 
	 * @return
	 */
	public static String getNormal2JiResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F514) + "  反馈回执").append("\n\n");

		buffer.append("您的消息我们已收到，更多动态请您关注：").append("\n");
		buffer.append("<a href='http://mucjsj.duapp.com/'>中央民族大学计算机协会</a>")
				.append("\n\n\n");

		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}

}
