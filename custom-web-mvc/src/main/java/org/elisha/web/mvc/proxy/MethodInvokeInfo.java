package org.elisha.web.mvc.proxy;

import org.elisha.orm.base.utiil.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class MethodInvokeInfo {

	private Map<String ,Method> methodMap = new HashMap<>();


	private final static Map<String , Method> OBJECT_METHOD = new HashMap<>();



	/**
	 * 目标类
	 */
	private Object target;



	static {
		List<Method> methods = ReflectionUtils.getMethods(Object.class);
		for (Method method : methods){
			OBJECT_METHOD.put(getMethodSignature(method)  , method);
		}
	}


	public MethodInvokeInfo(Object target ){
		this.target = target;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		List<Method> methods = ReflectionUtils.getMethods(target.getClass() , Object.class);
		for (Method method : methods){
			methodMap.put(getMethodSignature(method)  , method);
		}
	}


	/**
	 * 方法调用
	 * @param method
	 * @param args
	 * @return
	 */
	public Object invoke(Method method , Object[] args) throws InvocationTargetException, IllegalAccessException {
		method = getMethod(method);
		return method.invoke(target , args);
	}
	/**
	 * 获取方法签名
	 * @param method
	 * @return
	 */
	public static String getMethodSignature(Method method){
		return method.getName() + ":" + Arrays.asList(method.getParameterTypes()).stream().map(Class::getTypeName).collect(Collectors.joining(":"));
	}

	/**
	 * 获取目标方法
	 * @return
	 */
	public Method getMethod(Method method){
		//获取方法签名
		String methodSignature = getMethodSignature(method);
		method = OBJECT_METHOD.get(methodSignature);
		if (method == null){
			method = methodMap.get(methodSignature);
		}
		return method;
	}

	public Object getTarget() {
		return target;
	}

}
