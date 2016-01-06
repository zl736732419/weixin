package com.zheng.weixin.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 获取spring容器
 *
 * @author zhenglian
 * @time 2015年12月25日 下午2:14:10
 */
@Component
public class SpringManager implements
		ApplicationListener<ContextRefreshedEvent> {

	private static ApplicationContext ctx = null;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(ctx == null) {
			event.getApplicationContext();
		}
	}

	public static ApplicationContext getCtx() {
		return ctx;
	}
	
	public static Object getBean(String beanName, Class<?> clazz) {
		return ctx.getBean(beanName, clazz);
	}
	
}
