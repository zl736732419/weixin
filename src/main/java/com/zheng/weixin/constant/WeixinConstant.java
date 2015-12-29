package com.zheng.weixin.constant;

/**
 * 微信服务接口的一些常量值
 *
 * @author zhenglian
 * @data 2015年12月27日 下午6:05:03
 */
public class WeixinConstant {
	/**
	 * 获取accessToken的url
	 */
	public static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	/**
	 * 用户id
	 */
	public static String APP_ID = "wxd6a78e803b868407";
	/**
	 * 用户密匙
	 */
	public static String APP_SECRET = "c3f3a58e69f4bcd9dc29afc61b769c48";
	
	/**
	 * 自定义菜单
	 */
	public static String CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	

}
