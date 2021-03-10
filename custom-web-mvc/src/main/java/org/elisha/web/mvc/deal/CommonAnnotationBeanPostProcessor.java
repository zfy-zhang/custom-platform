package org.elisha.web.mvc.deal;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elisha.orm.base.utiil.ReflectionUtils;
import org.elisha.web.mvc.context.ApplicationContext;
import org.elisha.web.mvc.util.CommonUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class CommonAnnotationBeanPostProcessor implements BeanPostProcessor {


	private Map<String , List<ResourceElement>> resourceElementMap = new HashMap<>();

	private Map<String , InitializationElement> initializationElementMap = new HashMap<>();

	private Map<String , DestroyElement> destroyElementMap = new HashMap<>();



	@Override
	public Object jndiLoaderAfterPostProcessor(BeanWrapper beanWrapper) {
		initElements(beanWrapper);
		return null;
	}

	/**
	 * 初始化
	 */
	private void initElements(BeanWrapper beanWrapper) {
		Class<?> aClass = beanWrapper.getBean().getClass();
		if (!CommonUtils.isComponent(aClass)){
			return;
		}
		//初始化 @Resource 元信息
		List<Field> fields = ReflectionUtils.getFields(aClass, Object.class);
		List<ResourceElement> resourceElements = new ArrayList<>();
		for (Field field :fields){
			int modifiers = field.getModifiers();
			if (!Modifier.isStatic(modifiers) && field.isAnnotationPresent(Resource.class)){
				Resource resource = field.getAnnotation(Resource.class);
				resourceElements.add(new ResourceElement(field , StringUtils.isEmpty(resource.name()) ? field.getName() : resource.name()
						, beanWrapper.getBean() , beanWrapper ));
			}
		}
		resourceElementMap.put(beanWrapper.getName() ,resourceElements );
		//初始化 @PostConstruct @PreDestroy 元信息
		InitializationElement initializationElement = null;
		DestroyElement destroyElement = null;
		List<Method> methods = ReflectionUtils.getMethods(aClass, Object.class);
		for (Method method : methods){
			int modifiers = method.getModifiers();
			if (!Modifier.isStatic(modifiers)){
				 if (method.isAnnotationPresent(PostConstruct.class) ||method.isAnnotationPresent(PreDestroy.class)){
				 	Class<?>[] parameterTypes = method.getParameterTypes();
				 	if (parameterTypes.length > 0){
				 		throw new IllegalArgumentException(String.format("方法%s不能有参数"  , method.toGenericString()));
				    }
				 }

				 if ( method.isAnnotationPresent(PostConstruct.class)){
					 initializationElement  = new InitializationElement(method , beanWrapper.getBean());
				 }
				 if (method.isAnnotationPresent(PreDestroy.class)){
					 destroyElement = new DestroyElement(method , beanWrapper.getBean());
				 }
			}
		}
		initializationElementMap.put(beanWrapper.getName() , initializationElement);
		destroyElementMap.put(beanWrapper.getName() , destroyElement);

	}

	@Override
	public void initializationBeforePostProcessor(BeanWrapper beanWrapper) {
		List<ResourceElement> resourceElements = resourceElementMap.get(beanWrapper.getName());
		if (!CollectionUtils.isEmpty(resourceElements)) {
			for (ResourceElement element : resourceElements) {
				element.invoke();
			}
		}
	}

	@Override
	public void initializationPostProcessor(BeanWrapper beanWrapper) {
		InitializationElement initializationElement = initializationElementMap.get(beanWrapper.getName());
		if (initializationElement != null) {
			initializationElement.invoke();
		}
	}


	@Override
	public void destroyPostProcessor(BeanWrapper beanWrapper) {
		DestroyElement destroyElement = destroyElementMap.get(beanWrapper.getName());
		if (destroyElement != null) {
			destroyElement.invoke();
		}
	}


	public interface Element{
		void invoke();

	}

	/**
	 * 销毁元素
	 */
	public static class DestroyElement extends InitializationElement{

		public DestroyElement(Method method, Object target) {
			super(method, target);
		}
	}

	/**
	 * 初始化元素
	 */
	public static class InitializationElement implements Element{

		/**
		 * 初始化方法
		 */
		private Method method;

		/**
		 * 目标类
		 */
		private Object target;


		public InitializationElement(Method method , Object target){
			this.method = method;
			this.target = target;
		}

		@Override
		public void invoke() {
			try {
				method.invoke(target);
			}catch (Exception e){
				throw new IllegalArgumentException(e);
			}
		}
	}


	/**
	 * 依赖注入元素
	 */
	public static class ResourceElement implements Element {

		/**
		 * 字段
		 */
		private Field field;

		/**
		 * bean名称
		 */
		private String name;

		/**
		 * 目标类
		 */
		private Object target;

		/**
		 * bean包装
		 */
		private BeanWrapper beanWrapper;


		public ResourceElement(Field field , String name , Object target , BeanWrapper beanWrapper){
			this.field = field;
			this.name = name;
			this.target = target;
			this.beanWrapper = beanWrapper;
		}


		@Override
		public void invoke() {
			ReflectionUtils.set(field , target , ApplicationContext.resolveDependency(beanWrapper.getName() , name));
		}
	}
}
