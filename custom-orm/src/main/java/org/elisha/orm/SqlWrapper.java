package org.elisha.orm;

import java.util.List;

/**
 * @Description: sql包装
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class SqlWrapper {

	private String sql;


	private List<String> paramNames;

	public SqlWrapper(String sql, List<String> paramNames) {
		this.sql = sql;
		this.paramNames = paramNames;
	}

	public String getSql() {
		return sql;
	}

	public List<String> getParamNames() {
		return paramNames;
	}


	@Override
	public String toString() {
		return "SqlWrapper{" +
				"sql='" + sql + '\'' +
				", paramNames=" + paramNames +
				'}';
	}
}
