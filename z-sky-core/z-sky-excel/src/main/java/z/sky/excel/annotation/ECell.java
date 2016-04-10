package z.sky.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel列属性
 * <p>只有加了此注解的属性才会被写入Excel
 * @author jianming.zhou
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ECell {

	/** 列名 */
	String value() default "";
	/** 列宽 */
	int width() default 11;
	/** 时间格式 */
	String timeFormat() default "yyyy-MM-dd HH:mm:ss";
	
}
