package org.elisha.orm.base.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @Description: 代理工厂
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class ProxyFactory {


	/**
	 * 创建代理
	 * @param invocationHandler 拦截处理器
	 * @param tClass 接口class
	 * @param <T> 代理对象类型
	 * @return 返回一个代理对象
	 */
	public static <T>T createProxy(InvocationHandler invocationHandler ,Class<T>... tClass ){
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader() , tClass , invocationHandler);
	}

}
