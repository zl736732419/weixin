package com.zheng.weixin.ctx;

public class WeixinContext {
	private static String ACCESS_TOKEN;
	
	public static String getAccessToken() {
		return ACCESS_TOKEN;
	}
	
	public static void setAccessToken(String accessToken) {
		WeixinContext.ACCESS_TOKEN = accessToken;
	}

}
