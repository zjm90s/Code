package z.sky.push.base.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * SpringContext工具类
 * @author jianming.zhou
 *
 */
public class SpringContext implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}
	
	public static ApplicationContext getContext() {
		return context;
	}
	
	@SuppressWarnings("unchecked")
	public static <T>T getBean(String name) {
		return (T) context.getBean(name);
	}
	
	public static <T>T getBean(Class<T> requiredType) {
		return (T) context.getBean(requiredType);
	}

}
