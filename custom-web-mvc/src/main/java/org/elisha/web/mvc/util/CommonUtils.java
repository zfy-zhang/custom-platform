package org.elisha.web.mvc.util;

import org.elisha.web.mvc.header.annotation.Component;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class CommonUtils {

	/**
	 * 判断是否是组件
	 * @param aClass
	 * @return
	 */
	public static boolean isComponent(Class aClass){
		return aClass.isAnnotationPresent(Component.class);
	}
}
