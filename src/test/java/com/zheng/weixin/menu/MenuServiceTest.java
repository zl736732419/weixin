package com.zheng.weixin.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zheng.weixin.constant.WeixinConstant;
import com.zheng.weixin.ctx.WeixinContext;
import com.zheng.weixin.domain.WGroup;
import com.zheng.weixin.domain.WeixinMenu;
import com.zheng.weixin.enums.MenuType;
import com.zheng.weixin.kit.JsonKit;
import com.zheng.weixin.kit.MessageKit;
import com.zheng.weixin.quartz.task.RefreshAccessTokenTask;
import com.zheng.weixin.test.BaseServiceTest;

public class MenuServiceTest extends BaseServiceTest {
	
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
	public void testMenu() throws Exception {
		String json = createMenuJson();
		String url = WeixinConstant.CREATE_MENU.replaceAll("ACCESS_TOKEN", WeixinContext.getAccessToken());
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", "application/json");
		StringEntity entity = new StringEntity(json);
		post.setEntity(entity);
		
		CloseableHttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode >= 200 && statusCode < 300) {
			HttpEntity respEntity = response.getEntity();
			String content = EntityUtils.toString(respEntity);
			System.out.println(content);
		}
		
		if(response != null) {
			response.close();
		}
		
		if(client != null) {
			client.close();
		}
		
	}

	/**
	 * 创建一个菜单测试json
	 *
	 * @author zhenglian
	 * @data 2015年12月29日 下午9:39:00
	 * @return
	 */
	private String createMenuJson() {
		List<WeixinMenu> buttons = new ArrayList<>();
		WeixinMenu menu = new WeixinMenu();
		menu.setName("逗比趣事");
		menu.setType(MenuType.VIEW.getType());
		menu.setUrl("http://www.budejie.com/");
		buttons.add(menu);

		menu = new WeixinMenu();
		menu.setName("消息咨询");
		
		List<WeixinMenu> subBtns = new ArrayList<>();
		WeixinMenu subMenu = new WeixinMenu();
		subMenu.setName("新闻咨询");
		subMenu.setType(MenuType.CLICK.getType());
		subMenu.setKey("EVENT_NEWS");
		subBtns.add(subMenu);
		subMenu = new WeixinMenu();
		subMenu.setName("扫码带提示");
		subMenu.setType(MenuType.SCANCODE_WAITING.getType());
		subMenu.setKey("EVENT_SCAN_WAITING");
		subBtns.add(subMenu);
		
		menu.setSub_button(subBtns);
		
		buttons.add(menu);
		
		menu = new WeixinMenu();
		menu.setName("拍照发图");
		menu.setType(MenuType.PIC_PHOTO_OR_ALBUM.getType());
		menu.setKey("EVENT_PIC_PHOTO_OR_ALBUM");
		buttons.add(menu);
		
		Map<String, Object> map = new HashMap<>();
		map.put("button", buttons);
		
		String json = JsonKit.obj2Json(map);
		
		return json;
	}
	
	@Test
	public void testMap2Xml() throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("xml", "<person>123</person");
		String xml = MessageKit.map2Xml(map);
		System.out.println(xml);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testJson() {
		Map<String, List<WGroup>> map = new HashMap<>();
		List<WGroup> list = new ArrayList<>();
		WGroup group = new WGroup();
		group.setId(1);
		group.setName("测试组");
		group.setCount(10);
		list.add(group);
		map.put("groups", list);
		String json = JsonKit.obj2Json(map);
		System.out.println(json);
		
		Map<String, List<Map<String, Object>>> data = JsonKit.json2Obj(json, Map.class);
		System.out.println(data);
	}
	
	@Test
	public void testObj2JsonWithNull() {
		WGroup group = new WGroup();
		group.setId(1);
		String result = JsonKit.obj2Json(group);
		System.out.println(result);
	}
	
}
