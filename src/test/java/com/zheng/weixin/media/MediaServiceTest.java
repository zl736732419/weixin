package com.zheng.weixin.media;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zheng.weixin.ctx.WeixinContext;
import com.zheng.weixin.json.TemplateMsg;
import com.zheng.weixin.json.TemplateMsgModel;
import com.zheng.weixin.json.WeixinMedia;
import com.zheng.weixin.kit.MediaKit;
import com.zheng.weixin.kit.MessageKit;
import com.zheng.weixin.quartz.task.RefreshAccessTokenTask;
import com.zheng.weixin.test.BaseServiceTest;

public class MediaServiceTest extends BaseServiceTest {

	@Autowired
	private RefreshAccessTokenTask refreshTokenTask;
	
	@Before
	public void initAccessToken() {
		if(StringUtils.isBlank(WeixinContext.getAccessToken())) {
			System.out.println("accessToken is Null !!!!!!!!!!!!!!!");
			refreshTokenTask.refreshAccessToken();
		}
	}
	
	@Test
	public void test() {
		WeixinMedia media = MediaKit.uploadMedia("F://winphoto.jpg", "image");
		System.out.println(media.getMedia_id());
	}
	
	@Test
	public void testSendTemplateMsg() {
		TemplateMsg msg = new TemplateMsg();
		msg.setTouser("oumbewxauYaKGkZOrnbIG9Le8CNs");
		msg.setTemplate_id("wga5f0fQuSBmLWcm0xn0k_6UtA6EAR23SkVIVprl9BQ");
		msg.setUrl("http://www.baidu.com");
		Map<String, Object> map = new HashMap<>();
		TemplateMsgModel model = new TemplateMsgModel();
		model.setColor("#ff0000");
		model.setValue("123");
		map.put("num", model);
		msg.setData(map);
		
		MessageKit.sendTemplateMsg(msg);
	}
	
}
