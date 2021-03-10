package org.elisha.web.mvc.proxy;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class AopProxyContext {

	private final static AopProxyContext INSTANCE = new AopProxyContext();

	private final static   ClassLoader CLASS_LOADER;

	private final Map<Object , AopProxy> aopProxyMap = new ConcurrentHashMap<>();


	private final List<AdviceSupport> adviceSupports = new ArrayList<>();

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


	private ThreadLocal<Object> proxy = new ThreadLocal<>();


	static {
		CLASS_LOADER = Thread.currentThread().getContextClassLoader();
	}

	private AopProxyContext(){}

	/**
	 * 获取aop上下文对象
	 * @return
	 */
	public static AopProxyContext getInstance(){
		return INSTANCE;
	}



	/**
	 * 获取aop代理
	 * @param target
	 * @param interfaceClass
	 * @return
	 */
	public AopProxy getProxy(Object target , Class<?> interfaceClass){
		AopProxy aopProxy = aopProxyMap.get(target);
		if (aopProxy != null){
			return aopProxy;
		}
		synchronized (aopProxyMap){
			if (!aopProxyMap.containsKey(target)){
				aopProxy = createProxy(target, interfaceClass);
			}else {
				aopProxy = aopProxyMap.get(target);
			}
		}
		return aopProxy;
	}

	/**
	 * 注册通知
	 * @param adviceSupport
	 */
	public void registerAdvice(AdviceSupport adviceSupport){
		try {
			lock.writeLock().lock();
			this.adviceSupports.add(adviceSupport);
		}finally {
			lock.writeLock().unlock();
		}
	}


	/**
	 * 设置本地线程代理对象
	 * @param proxy
	 */
	void setLocalProxy(Object proxy){
		this.proxy.set(proxy);
	}

	/**
	 * 销毁本地线程代理
	 */
	void removeLocalProxy(){
		this.proxy.remove();
	}

	/**
	 * 获取本地线程代理对象
	 * @return
	 */
	public <P>P getLocalProxy(){
		return (P) this.proxy.get();
	}



	/**
	 * 创建aop proxy
	 * @param target
	 * @param interfaceClass
	 * @return
	 */
 	private AopProxy createProxy(Object target , Class<?> interfaceClass){
		return new JdkDynamicProxy(target, CLASS_LOADER, interfaceClass , this);
	}


	/**
	 * 匹配通知
	 * @param aClass
	 * @param method
	 * @return
	 */
	public List<Advice> matchAdvices(Class<?> aClass, Method method) {
		List<Advice> advices = new ArrayList<>();
		lock.readLock().lock();
		try {
			for (AdviceSupport adviceSupport : adviceSupports){
				if (adviceSupport.match(aClass , method)){
					advices.add(adviceSupport);
				}
			}
		}finally {
			lock.readLock().unlock();
		}
		return advices;
	}
}
