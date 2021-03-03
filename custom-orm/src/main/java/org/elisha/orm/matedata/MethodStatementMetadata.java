package org.elisha.orm.matedata;



import org.elisha.orm.ResultHandler;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description: 方法sql指令元数据
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class MethodStatementMetadata {

	/**
	 * 处理方法对象
	 */
	private Method method;


	/**
	 * sql
	 */
	private String sql;

	/**
	 * 指令类型
	 */
	private StatementType statementType;


	/**
	 * 参数名称
	 */
	private List<ParamMetadata> paramMetadataList;


	/**
	 * 结果处理器
	 */
	private ResultHandler resultHandler;



	/**
	 * 指令类型
	 */
	public enum StatementType{
		DML_AND_DCL,DLL_CUD,DLL_R
	}


	public ResultHandler getResultHandler() {
		return resultHandler;
	}

	public void setResultHandler(ResultHandler resultHandler) {
		this.resultHandler = resultHandler;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public StatementType getStatementType() {
		return statementType;
	}

	public void setStatementType(StatementType statementType) {
		this.statementType = statementType;
	}


	public List<ParamMetadata> getParamMetadataList() {
		return paramMetadataList;
	}

	public void setParamMetadataList(List<ParamMetadata> paramMetadataList) {
		this.paramMetadataList = paramMetadataList;
	}
}
