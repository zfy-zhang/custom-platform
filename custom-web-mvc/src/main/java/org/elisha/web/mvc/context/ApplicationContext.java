package org.elisha.web.mvc.context;

import org.apache.commons.collections4.CollectionUtils;
import org.elisha.web.mvc.deal.BeanPostProcessor;
import org.elisha.web.mvc.deal.BeanWrapper;

import javax.naming.*;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @Description: jndi application context
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class ApplicationContext {


	private static final Logger logger = Logger.getLogger(ApplicationContext.class.getName());

	private static Context CONTEXT;

	private static NamingException NAMING_EXCEPTION;

	private static List<BeanPostProcessor> BEAN_POST_PROCESSORS = new ArrayList<>();

	private static final Map<String, BeanWrapper> BEAN_WRAPPER_MAP = new HashMap<>();

	private static final Map<String, Object> BEANS_MAP = new ConcurrentHashMap<>();

	private static final List<String> BEAN_NAMES = new ArrayList<>();

	private static final List<Object> BEANS = new ArrayList<>();


	// 依赖处理   被依赖发 -> 依赖方
	private static final  Map<String , List<String>> DEPENDENCY_NAME_MAP = new ConcurrentHashMap<>();



	static {
		try {
			InitialContext initialContext = new InitialContext();
			CONTEXT = (Context) initialContext.lookup("java:comp/env");
			logger.log(Level.INFO, "JNDI上下文初始化完成");
			// 获取所有bean
			List<BeanWrapper> beanWrappers = lookupAll();
			for (BeanWrapper beanWrapper : beanWrappers) {
				BEANS.add(beanWrapper.getBean());
				BEAN_NAMES.add(beanWrapper.getName());
				Object bean = BEAN_WRAPPER_MAP.putIfAbsent(beanWrapper.getName(), beanWrapper);
				if (bean != null) {
					throw new IllegalStateException(String.format("bean名称为%s重复注册", beanWrapper.getName()));
				}
			}
			BEAN_POST_PROCESSORS = getBeanPostProcessors();
		} catch (NamingException e) {
			NAMING_EXCEPTION = e;
		}
	}




	/**
	 * 获取bean
	 *
	 * @param name bean名称
	 * @param <B>
	 * @return
	 */
	public static <B> B getBean(String name) {
		Object bean = BEANS_MAP.get(name);
		if (bean != null) {
			return (B) bean;
		}
		synchronized (BEANS_MAP) {
			if (!BEANS_MAP.containsKey(name)) {
				bean = doGetBean(name);
				BEANS_MAP.put(name, bean);
			} else {
				bean = BEANS_MAP.get(name);
			}
		}
		return (B) bean;
	}
	/**
	 * 查找所有bean
	 * @return
	 */
	private static List<BeanWrapper> lookupAll() {
		try {
			List<String> list = listNames("/");
			return list.stream().map(ApplicationContext::lookup).collect(Collectors.toList());
		}catch (Exception e){
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 查找jndi bean
	 * @return
	 */
	public static BeanWrapper lookup(String name){
		try {
			return new BeanWrapper(CONTEXT.lookup(name)  , name);
		}catch (Exception e){
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 执行获取bean
	 *
	 * @param name
	 * @return
	 */
	private static Object doGetBean(String name) {
		if (NAMING_EXCEPTION != null) {
			throw new IllegalStateException(NAMING_EXCEPTION);
		}
		BeanWrapper beanWrapper = BEAN_WRAPPER_MAP.get(name);
		if (beanWrapper == null) {
			throw new NoSuchElementException(String.format("没有bean名称为%s的bean", name));
		}
		//如果是代理类则直接返回
		if (Proxy.isProxyClass(beanWrapper.getBean().getClass())){
			return beanWrapper.getBean();
		}
		Object bean = null;
		//jndi加载后执行
		bean = applyJndiLoaderAfterPostProcessor(beanWrapper);
		if (bean != null) {
			return bean;
		}
		//初始化之前操作
		applyInitializationBeforePostProcessor(beanWrapper);
		//初始化操作
		applyInitializationPostProcessor(beanWrapper);
		//初始化之后操作
		bean = applyAfterInitializationPostProcessor(beanWrapper);
		if (bean != null){
			return bean;
		}
		return beanWrapper.getBean();
	}

	/**
	 * 应用初始化之后的后置处理器
	 * @param beanWrapper
	 */
	private static Object applyAfterInitializationPostProcessor(BeanWrapper beanWrapper) {
		for (BeanPostProcessor beanPostProcessor : BEAN_POST_PROCESSORS) {
			Object bean = beanPostProcessor.initializationAfterPostProcessor(beanWrapper);
			if (bean != null){
				return bean;
			}
		}
		return null;
	}


	/**
	 * 获取bean的后置处理器
	 *
	 * @return
	 */
	private static List<BeanPostProcessor> getBeanPostProcessors() {
		List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
		for (Object bean : BEANS) {
			if (bean instanceof BeanPostProcessor) {
				beanPostProcessors.add((BeanPostProcessor) bean);
			}
		}
		return beanPostProcessors;
	}

	/**
	 * 应用初始化后置处理器
	 *
	 * @param beanWrapper
	 */
	private static void applyInitializationPostProcessor(BeanWrapper beanWrapper) {
		for (BeanPostProcessor beanPostProcessor : BEAN_POST_PROCESSORS) {
			beanPostProcessor.initializationPostProcessor(beanWrapper);
		}
	}

	/**
	 * 应用初始化之前的后置操作
	 *
	 * @param beanWrapper
	 */
	private static void applyInitializationBeforePostProcessor(BeanWrapper beanWrapper) {
		for (BeanPostProcessor beanPostProcessor : BEAN_POST_PROCESSORS) {
			beanPostProcessor.initializationBeforePostProcessor(beanWrapper);
		}
	}

	/**
	 * 应该jndi加载之后的后置操作
	 *
	 * @param beanWrapper
	 * @return
	 */
	private static Object applyJndiLoaderAfterPostProcessor(BeanWrapper beanWrapper) {
		Object bean = null;
		for (BeanPostProcessor beanPostProcessor : BEAN_POST_PROCESSORS) {
			bean = beanPostProcessor.jndiLoaderAfterPostProcessor(beanWrapper);
			if (bean != null) {
				return bean;
			}
		}
		return bean;
	}

	/**
	 *应用销毁的后置操作
	 * @param beanWrapper
	 */
	private static void applyDestroyPostProcessors(BeanWrapper beanWrapper) {
		  for (BeanPostProcessor beanPostProcessor : BEAN_POST_PROCESSORS){
		  	    beanPostProcessor.destroyPostProcessor(beanWrapper);
		  }
	}


	/**
	 * 校验循环依赖
	 * @param primary 主bean名称 依赖方
	 * @param follow 从bean名称 被依赖方
	 */
	public static void checkDependency(String primary ,String follow){
		List<String> list = DEPENDENCY_NAME_MAP.get(primary);
		if (CollectionUtils.isEmpty(list)){
			list = new ArrayList<>();
			list.add(primary);
			DEPENDENCY_NAME_MAP.put(follow ,list );
		}else if (list.contains(follow)){
			throw new IllegalArgumentException(String.format("循环依赖 %s -> %s  %s  -> %s"  , primary , follow , follow , primary));
		}
	}

	/**
	 *
	 * @param primary 主bean名称 依赖方
	 * @param follow 从bean名称 被依赖方
	 * @return
	 */
	public static Object resolveDependency(String primary , String follow) {
		checkDependency(primary, follow);
		return getBean(follow);
	}

	/**
	 * 获取所有bean名称
	 *
	 * @return
	 */
	public static List<String> getBeanNames() {
		return BEAN_NAMES;
	}

	/**
	 * 获取bean列表
	 *
	 * @param prefix bean名称前置
	 * @return
	 */
	public static List<Object> getBeansByPrefix(String prefix) {
		List<Object> beans = new ArrayList<>();
		for (String beanName : getBeanNames()) {
			if (beanName.startsWith(prefix)) {
				beans.add(getBean(beanName));
			}
		}
		return beans;
	}


	/**
	 * 获取所有jndi中的bean名称
	 * @param name
	 * @return
	 * @throws Exception
	 */
	private static List<String> listNames(String name) throws Exception {
		NamingEnumeration<NameClassPair> e = CONTEXT.list(name);
		// 目录 - Context
		// 节点 -
		if (e == null) { // 当前 JNDI 名称下没有子节点
			return Collections.emptyList();
		}

		List<String> fullNames = new LinkedList<>();
		while (e.hasMoreElements()) {
			NameClassPair element = e.nextElement();
			String className = element.getClassName();
			Class<?> targetClass = Class.forName(className);
			if (Context.class.isAssignableFrom(targetClass)) {
				// 如果当前名称是目录（Context 实现类）的话，递归查找
				fullNames.addAll(listNames(element.getName()));
			} else {
				// 否则，当前名称绑定目标类型的话话，添加该名称到集合中
				String fullName = name.startsWith("/") ? element.getName() : name + "/" + element.getName();
				fullNames.add(fullName);
			}
		}
		return fullNames;
	}




	/**
	 * 关闭上下文
	 */
	public static void close() {
		for (String beanName : BEAN_NAMES){
			BeanWrapper beanWrapper = BEAN_WRAPPER_MAP.get(beanName);
			applyDestroyPostProcessors(beanWrapper);
		}
	}


}
