package org.elisha.orm;

import java.lang.reflect.Field;

/**
 * @Description: 字段包装类
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class FieldWrapper {

	/**
	 * 目标对象
	 */
	private Object target;


	/**
	 * 字段对象
	 */
	private Field field;




	public FieldWrapper(Object target, Field field) {
		this.target = target;
		this.field = field;
	}

	/**
	 * 设置字段值
	 * @param value
	 */
	public void setValue(Object value){
		try {
			field.setAccessible(true);
			field.set(target , value);
		}catch (Exception e){
			throw new IllegalStateException(e);
		}

	}

	/**
	 * 获取字段类型
	 * @return
	 */
	public Class<?> getFieldType(){
		return field.getType();
	}

}
