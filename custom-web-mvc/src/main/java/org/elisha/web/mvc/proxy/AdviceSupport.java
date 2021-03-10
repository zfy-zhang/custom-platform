package org.elisha.web.mvc.proxy;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class AdviceSupport implements Advice {

	private Advice advice;


	private Predicate predicate;


	private int order = 0;

	public AdviceSupport() {
	}

	public AdviceSupport(Advice advice , Predicate predicate){
		this.advice = advice;
		this.predicate = predicate;
	}


	public void setAdvice(Advice advice) {
		this.advice = advice;
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return order;
	}

	@Override
	public Object invoke(MethodProcessor methodProcessor) throws Throwable {
		return advice.invoke(methodProcessor);
	}


	public boolean match(Class targetClass , Method method){
		return predicate.match(targetClass, method);
	}
}
