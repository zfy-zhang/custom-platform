package org.elisha.orm;

/**
 * @Description: 大小写转换
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class CaseFieldMappingHandler extends HumpFieldMappingHandler{




	@Override
	public String columnNameToFieldName(String columnName) {
		return super.columnNameToFieldName(columnName.toLowerCase());
	}

	@Override
	public String fieldNameToColumnName(String fieldName) {
		return super.fieldNameToColumnName(fieldName).toUpperCase();
	}


	public static void main(String[] args) {
		System.out.print(new CaseFieldMappingHandler().columnNameToFieldName("AAA_SSSS"));
		System.out.println(new CaseFieldMappingHandler().fieldNameToColumnName("aaaBbb"));
	}
}
