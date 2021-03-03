package org.elisha.orm.matedata;


import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Description: 参数元数据
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class ParamMetadata {


	/**
	 * 是否是map
	 */
	private boolean isMap;

	/**
	 * 参数名称
	 */
	private String name;

	/**
	 * 参数对象
	 */
	private List<Field> fields;

	public ParamMetadata(String name, List<Field> fields , boolean isMap) {
		this.name = name;
		this.isMap = isMap;
		this.fields = fields;
	}

	/**
	 * 是否是一般的参数
	 * @return
	 */
	public boolean isGeneric(){
		return CollectionUtils.isEmpty(fields) && !isMap;
	}


	/**
	 * 是否是map类型
	 * @return
	 */
	public boolean isMap(){
		return isMap;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}



}
