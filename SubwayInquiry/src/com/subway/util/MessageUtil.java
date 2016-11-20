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
 * ��Ϣ������
 */
public class MessageUtil {

	/**
	 * ������Ϣ���ͣ��ı�
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * ������Ϣ���ͣ�����
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * ������Ϣ���ͣ�ͼ��
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * ������Ϣ���ͣ��ı�
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * ������Ϣ���ͣ�ͼƬ
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * ������Ϣ���ͣ�����
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * ������Ϣ���ͣ�����λ��
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * ������Ϣ���ͣ���Ƶ
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * ������Ϣ���ͣ�����
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * �¼����ͣ�subscribe(����)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * �¼����ͣ�unsubscribe(ȡ������)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * �¼����ͣ�CLICK(�Զ���˵�����¼�)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	/**
	 * ����΢�ŷ���������XML��
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) {
		// ����������洢��HashMap��
		Map<String, String> map = new HashMap<String, String>();

		// ��request��ȡ��������
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// ��ȡ������
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(inputStream);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		// �õ�xml��Ԫ��
		Element root = document.getRootElement();
		// �õ���Ԫ�ص������ӽڵ�
		List<Element> elementList = root.elements();

		// ���������ӽڵ�
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// �ͷ���Դ
		try {
			inputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		inputStream = null;

		return map;
	}

	/**
	 * �ı���Ϣ����ת����xml
	 *
	 * @param textMessage
	 *            �ı���Ϣ����
	 * @return xml
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * ������Ϣ����ת����xml
	 *
	 * @param musicMessage
	 *            ������Ϣ����
	 * @return xml
	 */
	public static String musicMessageToXml(MusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	/**
	 * ͼ����Ϣ����ת����xml
	 *
	 * @param newsMessage
	 *            ͼ����Ϣ����
	 * @return xml
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * ��չxstream��ʹ��֧��CDATA��
	 *
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// ������xml�ڵ��ת��������CDATA���
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
	 * �ж��Ƿ���QQ����
	 *
	 * @param content
	 * @return
	 */
	public static boolean isQqFace(String content) {
		boolean result = false;

		// �ж�QQ�����������ʽ
		String qqfaceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
		Pattern p = Pattern.compile(qqfaceRegex);
		Matcher m = p.matcher(content);
		if (m.matches()) {
			result = true;
		}
		return result;
	}

	/**
	 * emoji����ת��(hex -> utf-16)
	 *
	 * @param hexEmoji
	 * @return
	 */
	public static String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}

	public static String getWelcomes() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("��������ʶ�����˺��ټ���/:B-)").append("\n\n");
		buffer.append("��ӭ��עС�����ܸ���Ϊ������").append("\n\n");
		buffer.append(
				"�����������ƹ��ռ����������֪ʶ����ۺ������������������ߣ�����߻�Ա�����ˮƽΪ���Σ���������������Ӣ�����繤���ң�Ϊ����У԰�Ļ������ס�")
				.append("\n\n");
		buffer.append("�ظ���?����ʾ�˰����˵�").append("\n\n");
		buffer.append("�ظ��������ݿ��Ժ�С������������Ŷ~");
		return buffer.toString();
	}

	/**
	 * ���˵�
	 *
	 * @return
	 */
	public static String getMainMenu() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("���ã���ظ�����ѡ�����").append("\n\n");
		/*buffer.append("1  ���������").append("\n");
		buffer.append("2  ���Թ��ϱ���").append("\n");
		buffer.append("3  ����Ǣ̸").append("\n");
		buffer.append("4  �������繤����").append("\n");
		buffer.append("5  ������ѯ").append("\n");
		buffer.append("6  ����ʶ��").append("\n");
		buffer.append("7  �������").append("\n\n");*/
		buffer.append("1  ���������").append("\n");
		buffer.append("2  ���Թ��ϱ���").append("\n");

		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

	/**
	 * ��������� ���ջظ���"������...."
	 * 
	 * @return
	 */
	public static String get2JiResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F449) + " ���������").append("\n\n");

		buffer.append("����Ϊͬѧ���ṩ�˼������������ѵ����������¸�ʽ�ظ��ɱ����μӣ�").append("\n");
		buffer.append("������").append("\n");
		buffer.append("+ �����Ϣ+��ϵ��ʽ").append("\n\n");

		buffer.append("������Ϣ������ע��").append("\n");
		;
		buffer.append("<a href='http://mucjsj.duapp.com/'>���������ѧ�����Э��</a>")
				.append("\n\n");

		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

	/**
	 * ���Թ��ϱ��� ���ջظ���"���ޣ�...."
	 * 
	 * @return
	 */
	public static String getBaoXiuResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F528) + " ���Թ��ϱ���").append("\n\n");
/*
		buffer.append("�밴�����¸�ʽ�����ĵ��Թ��Ͻ���������").append("\n");
		buffer.append("���ޣ�").append("\n");
		buffer.append("+ �����Ϣ+��ϵ��ʽ").append("\n");
		buffer.append("+ ��ĵ��Թ����������뾡����ϸ��").append("\n\n");

		buffer.append("���ǽ��ڹ����Ϸ������ǵĴ�������������ע��").append("\n");
		;
		buffer.append("<a href='http://mucjsj.duapp.com/'>���������ѧ�����Э��</a>")
				.append("\n\n");
*/
		buffer.append("�����·��˵�����ѡ�񡰹��ϱ��ޡ�ģ����й��ϱ���\n����޷�ע�ᣬ���ȹ�ע������У԰���ˡ�����ƽ̨��΢�źţ�lgxylm���ڴ˹��ں���ѡ���޲�ע���˺ţ��˺󼴿��ڴ˹��ں������ܹ��ϱ��޷���").append("\n\n");
		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

	/**
	 * ����Ǣ̸ ���ջظ���"������...."
	 * 
	 * @return
	 */
	public static String getWaiLianResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("/:share ����Ǣ̸").append("\n\n");

		buffer.append(
				"�����Э���������ʿ����֯���ڱ������õĺ�����ϵ������Ǣ̸�������밴������ĸ�ʽ�ظ������ǽ������ʱ������ظ�����Ҳ���Ը����Ƿ��ʼ���mucjsj@163.com")
				.append("\n\n");

		buffer.append("������").append("\n");
		buffer.append("+ �����Ϣ����ϵ��ʽ").append("\n");
		buffer.append("+ ��Ҫ���͵�����").append("\n\n");

		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

	/**
	 * �������繤���� ���ջظ���"������...."
	 * 
	 * @return
	 */
	public static String getLianChuangResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F4BB) + " �������繤���Ҽ��").append("\n\n");

		buffer.append(
				"�������繤�����Ǽ����Э�������ġ���У�Լ��������Ȥ�ľ�Ӣ�����, ���ִ�����������Ϊ���С�����Ϣ����ѧԺΪ��Ҫ�Ƽ���ص�ѧ���������롢��֯��������������о��ͷ�չ�������������������ҵ�ĿƼ��͡�ʵ���͵�ѧ����֯�����ɲ�����һ�����нϸ߼��������ˮƽ��ѧ����Ӣ��")
				.append("\n\n");

		buffer.append(
				"�����Լ����������Ũ�����Ȥ������������ǰɣ�����רҵ���꼶�����Ƕ�ϣ�����������룡 " + emoji(0x2764))
				.append("\n");
		buffer.append("�������¸�ʽ�ظ������Ϣ������������˵�Ļ���").append("\n\n");

		buffer.append("������").append("\n");
		buffer.append("+ ��Ҫ���͵�����").append("\n\n");

		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

	/**
	 * ������ѯ���ܽ�����Ϣ
	 * 
	 * @return
	 */
	public static String getBusResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("������ѯʹ��ָ��").append("\n\n");

		buffer.append(emoji(0x1F31F) + " ��ѯ���й�����·").append("\n");
		buffer.append("��ʽ�����У���·����").append("\n");
		buffer.append("���磺������2·").append("\n\n");

		buffer.append(emoji(0x1F31F) + " ��ѯ���й����ݳ˷���").append("\n");
		buffer.append("��ʽ�����У�������յ�").append("\n");
		buffer.append("���磺������κ�������찲��").append("\n\n");

		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

	/**
	 * ����ʶ���ܽ�����Ϣ
	 * 
	 * @return
	 */
	public static String getFaceResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F466) + " ����ʶ��ʹ��ָ��").append("\n\n");

		buffer.append("����һ����������Ƭ��Сq���ܰ�����������塢���䡢�Ա����ϢŶ").append("\n");
		buffer.append("�����������ǲ��ǳ���̫�ż� " + emoji(0x1F383)).append("\n\n");

		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

	/**
	 * ������ྐྵ��ܽ�����Ϣ
	 * 
	 * @return
	 */
	public static String getChattingResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F61C) + " �������ʹ��˵��").append("\n\n");

		buffer.append("��Ͼ���ģ�����ྰɣ����ʱش����磺").append("\n");
		buffer.append(emoji(0x1F449) + " ����Ц�� ").append("\n");
		buffer.append(emoji(0x1F449) + " �½���ʲô����� ").append("\n");
		buffer.append(emoji(0x1F449) + " ���������� ").append("\n");
		buffer.append(emoji(0x1F449) + " ��Ʊ�绰").append("\n\n");

		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

	/**
	 * ������ִ��������
	 * 
	 * @return
	 */
	public static String getNormalLianChuangResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F514) + "  ������ִ").append("\n\n");

		buffer.append("������Ϣ�������յ������ǻᾡ���������Ϣ���д����뼰ʱ��ע�������繤���ҵĶ�̬��").append(
				"\n\n");

		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

	/**
	 * ������ִ��������
	 * 
	 * @return
	 */
	public static String getNormalWaiLianResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F514) + "  ������ִ").append("\n\n");

		buffer.append("������Ϣ�������յ������ǻᾡ���������Ϣ���д����뼰ʱ��ע�����Э��Ķ�̬��").append("\n\n");

		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

	/**
	 * ������ִ�����ޣ�
	 * 
	 * @return
	 */
	public static String getNormalBaoXiuResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F514) + "  ������ִ").append("\n\n");

		buffer.append("���ı�����Ϣ�������յ������ǽ��ڹ����Ϸ������ǵĴ�������������ע��").append("\n");
		buffer.append("<a href='http://mucjsj.duapp.com/'>���������ѧ�����Э��</a>")
				.append("\n\n\n");

		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

	/**
	 * ������ִ��������
	 * 
	 * @return
	 */
	public static String getNormal2JiResponseMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(emoji(0x1F514) + "  ������ִ").append("\n\n");

		buffer.append("������Ϣ�������յ������ද̬������ע��").append("\n");
		buffer.append("<a href='http://mucjsj.duapp.com/'>���������ѧ�����Э��</a>")
				.append("\n\n\n");

		buffer.append("�ظ���?����ʾ�˰����˵�");
		return buffer.toString();
	}

}
