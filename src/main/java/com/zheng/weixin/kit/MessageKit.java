package com.zheng.weixin.kit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.zheng.weixin.constant.WeixinConstant;
import com.zheng.weixin.ctx.WeixinContext;
import com.zheng.weixin.json.TemplateMsg;

/**
 * 解析微信post过来的数据 响应的数据只能通过inputstream的方式来获取
 *
 * @author zhenglian
 * @data 2016年1月2日 上午9:20:17
 */
public class MessageKit {

	/**
	 * 文本消息列表
	 */
	public static Map<String, Object> textMsgs = new HashMap<>();
	static {
		textMsgs.put("hello", "world");
		textMsgs.put("123", "456");
		textMsgs.put("run", "good luck to you !");
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> req2Map(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			String text = getMsgContent(request);
			Document document = DocumentHelper.parseText(text);
			Element root = document.getRootElement();
			List<Element> elements = root.elements();
			for (Element element : elements) {
				map.put(element.getName(), element.getTextTrim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取微信服务器反馈的xml文本内容
	 *
	 * @author zhenglian
	 * @data 2016年1月2日 上午9:50:01
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private static String getMsgContent(HttpServletRequest request)
			throws IOException {
		StringBuilder builder = new StringBuilder();
		InputStream input = request.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}

		return builder.toString();
	}

	/**
	 * 根据反馈的消息类型创建不同的响应消息
	 *
	 * @author zhenglian
	 * @data 2016年1月2日 上午10:25:31
	 * @param map
	 * @return
	 * @throws IOException
	 */
	public static String createRespMsg(Map<String, Object> map)
			throws IOException {
		Map<String, Object> respMsg = new HashMap<>();
		respMsg.put("ToUserName", map.get("FromUserName"));
		respMsg.put("FromUserName", map.get("ToUserName"));
		respMsg.put("CreateTime", new Date().getTime() + "");
		String msgType = (String) map.get("MsgType");
		respMsg.put("MsgType", map.get("MsgType"));
		String msg = null;
		if (msgType != null) {
			if (msgType.equals(WeixinConstant.MSG_TYPE_TEXT)) {
				createTextRespMsg(respMsg, map);
			} else if (msgType.equals(WeixinConstant.MSG_TYPE_IMAGE)) {
				createImageRespMsg(respMsg, map);
			}

			msg = map2Xml(respMsg);
		} else {
			msg = "";
		}

		return msg;
	}

	/**
	 * 创建图片响应消息
	 *
	 * @author zhenglian
	 * @data 2016年1月3日 上午11:04:15
	 * @param respMsg
	 * @param map
	 */
	private static void createImageRespMsg(Map<String, Object> respMsg,
			Map<String, Object> map) {
		String mediaId = (String) map.get("MediaId");
		System.out.println(mediaId);
		respMsg.put("MediaId", mediaId);
	}

	/**
	 * 根据map创建xml
	 *
	 * @author zhenglian
	 * @data 2016年1月2日 上午10:40:42
	 * @param respMsg
	 * @return
	 * @throws IOException
	 */
	public static String map2Xml(Map<String, Object> respMsg)
			throws IOException {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");
		Set<String> keys = respMsg.keySet();
		for (String key : keys) {
			root.addElement(key).addText((String) respMsg.get(key));
		}
		StringWriter writer = new StringWriter();
		XMLWriter xw = new XMLWriter(writer);
		xw.setEscapeText(false);
		xw.write(document);
		return writer.toString();
	}

	/**
	 * 创建文本消息响应信息
	 *
	 * @author zhenglian
	 * @data 2016年1月2日 上午10:37:11
	 * @param respMsg
	 * @param map
	 */
	public static void createTextRespMsg(Map<String, Object> respMsg,
			Map<String, Object> map) {
		String key = (String) map.get("Content");
		if (textMsgs.containsKey(key)) {
			respMsg.put("Content", "你发送了消息：" + textMsgs.get(key));
		} else {
			respMsg.put("Content", "你输入的消息不正确!");
		}
	}

	/**
	 * 发送模版消息
	 *
	 * @author zhenglian
	 * @data 2016年1月3日 上午11:46:33
	 * @param msg
	 */
	public static void sendTemplateMsg(TemplateMsg msg) {
		String url = WeixinConstant.SEND_TEMPLATE_MSG.replaceAll(
				"ACCESS_TOKEN", WeixinContext.getAccessToken());
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse resp = null;
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json;charset=utf-8");
		String json = JsonKit.obj2Json(msg);
		System.out.println(json);
		StringEntity entity = new StringEntity(json, ContentType.create(
				"application/json", Charset.forName("UTF-8")));
		post.setEntity(entity);
		try {
			resp = client.execute(post);
			int sc = resp.getStatusLine().getStatusCode();
			if(sc >= 200 && sc < 300) {
				String result = EntityUtils.toString(resp.getEntity());
				System.out.println(result);
			}else {
				System.out.println("获取响应消息异常!状态码：" + sc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(resp != null) {
				try {
					resp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
