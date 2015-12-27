package com.zheng.weixin.service.impl;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zheng.weixin.kit.BasicSysKit;
import com.zheng.weixin.service.IWeixinService;

@Service
public class WeixinServiceImpl implements IWeixinService {

	@Override
	public boolean validate(String token, String timestamp, String nonce, String signature) {
		String[] arr = new String[] {token, timestamp, nonce};
		//1.字典排序
		Arrays.sort(arr);
		//2.拼接成字符串
		String str = StringUtils.join(arr, "");
		//3.sha1加密
		String result = BasicSysKit.sha1(str);
		
		return signature.equals(result);
	}

}
