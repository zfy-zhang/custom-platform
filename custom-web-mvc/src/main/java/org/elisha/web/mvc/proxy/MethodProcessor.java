package org.elisha.web.mvc.proxy;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class MethodProcessor implements AutoCloseable {

	/**
	 * 代理对象
	 */
	private Object proxy;



	/**
	 * 执行参数
	 */
	private Object[] args;


	/**
	 * 通知
	 */
	private List<Advice> advices;

	/**
	 * 通知执行位置
	 */
	private int index = -1;

	/**
	 * 调用信息
	 */
	private MethodInvokeInfo methodInvokeInfo;

	/**
	 * 代理方法
	 */
	private ThreadLocal<Method> proxyMethod  = new ThreadLocal<>();

	public MethodProcessor(Object proxy, MethodInvokeInfo methodInvokeInfo, Object[] args, List<Advice> advices) {
		this.proxy = proxy;
		this.args = args;
		this.advices = advices;
		this.methodInvokeInfo = methodInvokeInfo;
	}

	public void setProxyMethod(Method method){
		proxyMethod.set(method);
	}


	public Object getProxy() {
		return proxy;
	}

	public Object getTargetObject() {
		return methodInvokeInfo.getTarget();
	}

	public Method getMethod() {
		return methodInvokeInfo.getMethod(proxyMethod.get());
	}


	public Method getProxyMethod(){
		return proxyMethod.get();
	}

	public Object[] getArgs() {
		return args;
	}

	public Object invoke() throws Throwable {
		if (CollectionUtils.isEmpty(advices) || index == advices.size() - 1) {
			return methodInvokeInfo.invoke(proxyMethod.get() ,args);
		}
		index ++;
		Advice advice = advices.get(index);
		return advice.invoke(this);
	}


	@Override
	public void close() throws IOException {
		proxyMethod.remove();
	}
}
