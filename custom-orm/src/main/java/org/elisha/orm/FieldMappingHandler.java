package org.elisha.orm;

/**
 * @Description: 字段映射处理器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public interface FieldMappingHandler {

	/**
	 * 列名称转换为字段名称
	 * @param columnName
	 * @return
	 */
	String columnNameToFieldName(String columnName);


	/**
	 * 字段名称转换为列名称
	 * @param fieldName
	 * @return
	 */
	String fieldNameToColumnName(String fieldName);
}
