package com.zheng.weixin.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zheng.weixin.ctx.WeixinContext;
import com.zheng.weixin.service.IWeixinService;

/**
 * 微信开发页面交互接口
 *
 * @author zhenglian
 * @data 2015年12月27日 下午4:03:03
 */
@Controller
public class WeixinController extends BaseController {
	private static final String WEIXIN_TOKEN = "weixin_zl";
	
	@Autowired
	private IWeixinService service;
	
	@RequestMapping("/init")
	public void init() {
//		signature	 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
//		timestamp	 时间戳
//		nonce	 随机数
//		echostr
		
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echoStr = request.getParameter("echostr");
		
		//校验微信接口
		boolean result = service.validate(WEIXIN_TOKEN, timestamp, nonce, signature);
		if(result) { //验证成功，返回echostr
			try {
				response.getWriter().println(echoStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping("/accessToken")
	public void getAccessToken() {
		try {
			response.getWriter().println(WeixinContext.getAccessToken());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
