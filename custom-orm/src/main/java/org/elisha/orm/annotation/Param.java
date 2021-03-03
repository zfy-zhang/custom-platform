package org.elisha.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 查询参数
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

	/**
	 * 参数名称
	 * @return
	 */
	String value();

	/**
	 * 是否是实体类
	 * @return
	 */
	boolean entity() default false;
}
