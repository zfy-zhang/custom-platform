package org.elisha.web.mvc.deal;

import java.util.HashMap;

/**
 * @Description: meta information
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class AttributeAccessor extends HashMap<String , Object> {


	/**
	 * 获取属性
	 * @param name
	 * @param <T>
	 * @return
	 */
	public <T>T getAttribute(String name){
		return (T) this.get(name);
	}


	/**
	 * 设置属性
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name , Object value){
		this.put(name , value);
	}
}
