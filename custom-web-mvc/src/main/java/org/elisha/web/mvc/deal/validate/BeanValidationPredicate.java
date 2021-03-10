package org.elisha.web.mvc.deal.validate;

import org.elisha.web.mvc.proxy.Predicate;

import javax.validation.Constraint;
import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: meta information
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class BeanValidationPredicate implements Predicate {


	private final Map<Method ,Boolean > matchMap = new ConcurrentHashMap<>();

	@Override
	public boolean match(Class targetClass, Method method) {
		return doMatch(method);
	}


	/**
	 * 执行匹配
	 * @param method
	 * @return
	 */
	private boolean doMatch(Method method){
		Boolean fls = matchMap.get(method);
		if (fls != null){
			return fls;
		}
		synchronized (matchMap) {
			if (!matchMap.containsKey(method)) {
				fls = false;
				Annotation[][] parameterAnnotations = method.getParameterAnnotations();
				for (Annotation[] annotations  : parameterAnnotations){
					for (Annotation annotationItem : annotations){
						if (annotationItem.annotationType().isAnnotationPresent(Constraint.class)
								|| annotationItem.annotationType() == Constraint.class
								|| annotationItem.annotationType() == Valid.class){
							fls = true;
							break;
						}
					}
				}
				matchMap.put(method , fls);
			}else {
				fls = matchMap.get(method);
			}
		}
		return fls;
	}

}
