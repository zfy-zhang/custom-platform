package org.elisha.orm;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

/**
 * @Description: 驼峰命名转换
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class HumpFieldMappingHandler implements FieldMappingHandler {

	private final static Converter<String, String> FIELD_NAME_TO_COLUMN_NAME_CONVERTER = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);

	private final static Converter<String, String> COLUMN_NAME_TO_FIELD_CONVERTER = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);

	@Override
	public String columnNameToFieldName(String columnName) {
		return COLUMN_NAME_TO_FIELD_CONVERTER.convert(columnName);
	}

	@Override
	public String fieldNameToColumnName(String fieldName) {
		return FIELD_NAME_TO_COLUMN_NAME_CONVERTER.convert(fieldName);
	}

}
