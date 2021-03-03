package org.elisha.orm;

import org.elisha.orm.base.utiil.ReflectionUtils;

import java.sql.ResultSet;
import java.util.Collection;

/**
 * @Description: 简单的结果处理
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public  class SimpleResultHandler extends AbstractResultHandler {




	public SimpleResultHandler(ResultMetadata resultMetadata) {
		super(resultMetadata);
	}


	@Override
	protected Object tranFromObject0(ResultSet resultSet) throws Throwable {
		//处理 void 返回类型
		if (ReflectionUtils.isVoid(resultMetadata.getReturnElementType() )){
			return null;
		}
		Collection<Object> wrapperObject = resultMetadata.getWrapperObject();
		Object result = null;
		int elementCount = 1;
		while (resultSet.next()){
			result = getValue(resultMetadata.getReturnElementType(), resultSet, 1);
			if (wrapperObject != null) {
				wrapperObject.add(result);
			}
		}
		checkResultCount(elementCount , wrapperObject);
		return wrapperObject != null ? wrapperObject : result;
	}
}
