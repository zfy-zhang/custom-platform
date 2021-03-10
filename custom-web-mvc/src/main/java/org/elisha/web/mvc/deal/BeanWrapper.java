package org.elisha.web.mvc.deal;


/**
 * @Description: bean
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class BeanWrapper extends AttributeAccessor{

	private Object bean;

	private String name;


	public BeanWrapper(Object bean, String name) {
		this.bean = bean;
		this.name = name;
	}

	public Object getBean() {
		return bean;
	}

	public String getName() {
		return name;
	}

}
