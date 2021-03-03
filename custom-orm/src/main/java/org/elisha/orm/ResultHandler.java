package org.elisha.orm;

import java.sql.ResultSet;

/**
 * 结果处理器
 * @author hukangning
 */
/**
 * @Description: 抽象的结果处理器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public interface ResultHandler {

	/**
	 * 转换对象
	 * @param resultSet
	 * @return
	 * @throws Throwable
	 */
	Object tranFromObject(ResultSet resultSet) throws Throwable;


	/**
	 * 获取响应结果
	 * @return
	 */
	ResultMetadata getResultMetadata();
}
