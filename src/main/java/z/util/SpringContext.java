package z.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringContext工具类
 * @author jianming.zhou
 *
 */
@Component
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
