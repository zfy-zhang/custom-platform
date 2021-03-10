package org.elisha.web.mvc.deal;

/**
 * @Description: bean的后置处理器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public interface BeanPostProcessor {


	/**
	 * jndi加载后执行
	 * @param beanWrapper
	 * @return
	 */
	default Object jndiLoaderAfterPostProcessor(BeanWrapper beanWrapper){
		return null;
	}


	/**
	 * 初始化之前操作
	 * @param beanWrapper
	 */
	void   initializationBeforePostProcessor(BeanWrapper beanWrapper);


	/**
	 * 初始化操作
	 * @param beanWrapper
	 */
	void   initializationPostProcessor(BeanWrapper beanWrapper);


	/**
	 * 初始化之后操作
	 * @param beanWrapper
	 */
	default Object initializationAfterPostProcessor(BeanWrapper beanWrapper){
		return null;
	}


	/**
	 * 销毁操作
	 * @param beanWrapper
	 */
	default void  destroyPostProcessor(BeanWrapper beanWrapper){

	}



}
