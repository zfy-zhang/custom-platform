package org.elisha.orm;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 结果处理器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class ComplexResultHandler extends AbstractResultHandler {


	public ComplexResultHandler(ResultMetadata resultMetadata) {
		super(resultMetadata);
	}

	@Override
	protected Object tranFromObject0(ResultSet resultSet) throws Throwable {
		Collection<Object> wrapperObject = resultMetadata.getWrapperObject();
		Object elementObject = resultMetadata.getElementObject();
		//获取所有的列名称
		List<String> columnNames = getColumnNames(resultSet);
		//便利结果并处理
		int elementCount = 1;
		while (resultSet.next()){
			//map
			if (elementObject instanceof  Map){
				for (String columnName : columnNames){
					resultMetadata.setValue(resultSet , this , elementObject , columnName);
				}
			}
			//entity
			else {
				for (Field field : resultMetadata.getFields()){
					resultMetadata.setValue(resultSet , this , new FieldWrapper( elementObject ,field ) , field.getName() );
				}
			}
			//包装类
			if (wrapperObject != null){
				wrapperObject.add(elementObject);
				elementObject = resultMetadata.getElementObject();
			}
			elementCount ++;
		}

		//校验响应结果类型和数量和实际数量是否一致
		if (elementCount > 1 && wrapperObject == null){
			throw new SQLException("你预期要查询一个元素，但实际查出多个元素");
		}
		return wrapperObject != null ? wrapperObject : elementObject;
	}


	/**
	 * 获取列名的集合
	 * @param resultSet
	 * @return
	 */
	private List<String> getColumnNames(ResultSet resultSet) throws SQLException {
		List<String> columnNames = new ArrayList<>();
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		for (int i = 1 , length =  resultSetMetaData.getColumnCount() ; i <= length ; i++ ){
			columnNames.add(resultSetMetaData.getColumnName(i));
		}
		return columnNames;
	}



}
