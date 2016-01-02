package com.zheng.weixin.kit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.zheng.weixin.constant.WeixinConstant;

/**
 * 解析微信post过来的数据
 *响应的数据只能通过inputstream的方式来获取
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
			for(Element element : elements) {
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
	private static String getMsgContent(HttpServletRequest request) throws IOException {
		StringBuilder builder = new StringBuilder();
		InputStream input = request.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = null;
		while((line = reader.readLine()) != null) {
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
	public static String createRespMsg(Map<String, Object> map) throws IOException {
		Map<String, Object> respMsg = new HashMap<>();
		respMsg.put("ToUserName", map.get("FromUserName"));
		respMsg.put("FromUserName", map.get("ToUserName"));
		respMsg.put("CreateTime", new Date().getTime()+"");
		String msgType = (String) map.get("MsgType"); 
		respMsg.put("MsgType", map.get("MsgType"));
		String msg = null;
		if(msgType != null) {
			if(msgType.equals(WeixinConstant.MSG_TYPE_TEXT)) {
				createTextRespMsg(respMsg, map);
			}
			
			msg = map2Xml(respMsg);
		} else {
			msg = "";
		}
		
		return msg;
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
	private static String map2Xml(Map<String, Object> respMsg) throws IOException {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");
		Set<String> keys = respMsg.keySet();
		for(String key : keys) {
			root.addElement(key).addText((String) respMsg.get(key));
		}
		StringWriter writer = new StringWriter();
		document.write(writer);
		
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
	private static void createTextRespMsg(Map<String, Object> respMsg, Map<String, Object> map) {
		String key = (String) map.get("Content");
		if(textMsgs.containsKey(key)) {
			respMsg.put("Content", "你发送了消息：" + textMsgs.get(key));
		}else {
			respMsg.put("Content", "你输入的消息不正确!");
		}
	}
	
}
