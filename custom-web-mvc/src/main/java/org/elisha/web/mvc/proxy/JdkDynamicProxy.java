package org.elisha.web.mvc.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class JdkDynamicProxy implements AopProxy ,  InvocationHandler {

	/**
	 * 目标对象
	 */
	private Object target;

	/**
	 * 目标接口
	 */
	private Class clazz;

	/**
	 * class 加载器
	 */
	private ClassLoader classLoader;


	/**
	 * aop 上下文
	 */
	private AopProxyContext aopProxyContext;


	/**
	 * 方法映射
	 */
	private MethodInvokeInfo methodInvokeInfo;



	public JdkDynamicProxy(Object target , ClassLoader classLoader , Class clazz , AopProxyContext aopProxyContext){
		this.target = target;
		this.classLoader = classLoader;
		this.clazz = clazz;
		this.aopProxyContext = aopProxyContext;
		initMetadata();
	}

	/**
	 * 初始化元数据
	 */
	private void initMetadata() {
		methodInvokeInfo = new MethodInvokeInfo(this.target);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			//设置本地代理
			aopProxyContext.setLocalProxy(proxy);
			List<Advice> advices = aopProxyContext.matchAdvices(target.getClass(), methodInvokeInfo.getMethod(method));
			//排序
			advices = shortAdvices(advices);
			try (MethodProcessor methodProcessor = new MethodProcessor(proxy, methodInvokeInfo, args, advices)) {
				methodProcessor.setProxyMethod(method);
				return methodProcessor.invoke();
			}
		}finally {
			aopProxyContext.removeLocalProxy();
		}
	}

	private List<Advice> shortAdvices(List<Advice> advices) {
		return advices.stream().map(item -> (AdviceSupport)item).sorted(Comparator.comparing(AdviceSupport::getOrder)).collect(Collectors.toList());

	}

	@Override
	public Object getTarget() {
		return this.target;
	}


	@Override
	public Object getProxy() {
		return Proxy.newProxyInstance(classLoader , new Class[]{clazz} , this);
	}

	@Override
	public List<Advice> getAdvice() {
		return null;
	}
}
