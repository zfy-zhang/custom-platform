package org.elisha.web.mvc.proxy;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
@FunctionalInterface
public interface Predicate {

	 boolean match(Class targetClass, Method method);
}
