package com.zheng.weixin.quartz.task;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Logger;

import com.zheng.weixin.constant.WeixinConstant;
import com.zheng.weixin.ctx.WeixinContext;
import com.zheng.weixin.json.AccessToken;
import com.zheng.weixin.json.ErrorResp;
import com.zheng.weixin.kit.JsonKit;

/**
 * 定时刷新accessToken的任务
 * 微信每隔7200秒会重新生成accessToken，所以需要定时刷新accessToken值
 *
 * @author zhenglian
 * @data 2015年12月27日 下午6:12:11
 */
@Component
public class RefreshAccessTokenTask {
	private static Logger logger = (Logger) LoggerFactory.getLogger(RefreshAccessTokenTask.class);
	public void refreshAccessToken() {
		//通过httpclient请求accessToken
		CloseableHttpClient client = null;
		HttpGet get = null;
		CloseableHttpResponse response = null;
		String content = null;
		try {
			client = HttpClients.createDefault();
			String url = WeixinConstant.ACCESS_TOKEN_URL;
			url = url.replaceAll("APPID", WeixinConstant.APP_ID)
					.replaceAll("APPSECRET", WeixinConstant.APP_SECRET);
			get = new HttpGet(url);
			response = client.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode >= 200 && statusCode <= 300) {
				HttpEntity entity = response.getEntity();
				content = EntityUtils.toString(entity);
				try {
					AccessToken accessToken = JsonKit.json2Obj(content, AccessToken.class);
					WeixinContext.setAccessToken(accessToken.getAccess_token());
				} catch (Exception e) {
					ErrorResp error = JsonKit.json2Obj(content, ErrorResp.class);
					refreshAccessToken(); //如果发生错误就重新运行
					logger.warn("获取accessToken发生错误:" + error.getErrcode() + "," + error.getErrmsg());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					ErrorResp error = JsonKit.json2Obj(content, ErrorResp.class);
					
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
