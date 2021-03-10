package org.elisha.web.mvc.deal.validate;


import org.apache.commons.lang.ArrayUtils;
import org.elisha.web.mvc.deal.BeanPostProcessor;
import org.elisha.web.mvc.deal.BeanWrapper;
import org.elisha.web.mvc.proxy.AdviceSupport;
import org.elisha.web.mvc.proxy.AopProxy;
import org.elisha.web.mvc.proxy.AopProxyContext;
import org.elisha.web.mvc.util.CommonUtils;


/**
 * @Description: meta information
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class BeanValidationBeanPostProcessor implements BeanPostProcessor {


	private static final String PROXY = "proxy";


	@Override
	public Object jndiLoaderAfterPostProcessor(BeanWrapper beanWrapper) {
		Class<?> aClass = beanWrapper.getBean().getClass();
		Class<?>[] interfaces = aClass.getInterfaces();
		//如果没有接口 则不进行代理
		if (!ArrayUtils.isEmpty(interfaces) && CommonUtils.isComponent(aClass)){
			BeanValidationAdvice beanValidationAdvice = new BeanValidationAdvice(aClass);
			AopProxyContext aopProxyContext = AopProxyContext.getInstance();
			AdviceSupport adviceSupport = new AdviceSupport(beanValidationAdvice , new BeanValidationPredicate());
			aopProxyContext.registerAdvice(adviceSupport);
			if (!beanWrapper.containsKey(PROXY)) {
				AopProxy proxy = aopProxyContext.getProxy(beanWrapper.getBean(), interfaces[0]);
				beanWrapper.setAttribute(PROXY, proxy.getProxy());
			}
		}
		return null;
	}

	@Override
	public void initializationBeforePostProcessor(BeanWrapper beanWrapper) {

	}

	@Override
	public void initializationPostProcessor(BeanWrapper beanWrapper) {

	}

	@Override
	public Object initializationAfterPostProcessor(BeanWrapper beanWrapper) {
		Object proxy = beanWrapper.getAttribute(PROXY);
		if (proxy != null){
			return proxy;
		}
		return null;
	}
}
