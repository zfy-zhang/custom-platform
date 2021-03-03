package org.elisha.orm;

import org.elisha.orm.base.utiil.ReflectionUtils;
import org.elisha.orm.matedata.MethodStatementMetadata;
import org.elisha.orm.matedata.ParamMetadata;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
* 预编译支持类  
* @author : KangNing Hu
*/
/**
 * @Description: 抽象的结果处理器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class PreparedStatementSupport {

	private final static Logger logger =Logger.getLogger("");


	/**
	 * 解析响应结果
	 * @param resultSet 结果集
	 * @param methodStatementMetadata 方法元信息
	 * @return 返回查询结果
	 * @throws Throwable
	 */
	protected Object parseResult(Object resultSet, MethodStatementMetadata methodStatementMetadata) throws Throwable {
		ResultMetadata resultMetadata = methodStatementMetadata.getResultHandler().getResultMetadata();
		if (resultSet instanceof Boolean ){
			if (resultMetadata.getReturnElementType() == boolean.class || resultMetadata.getReturnElementType() == Boolean.class){
				return resultSet;
			}
			return null;
		}
		if (resultSet instanceof Integer){
			if (resultMetadata.getReturnElementType() == Integer.class || resultMetadata.getReturnElementType() == int.class){
				return resultSet;
			}
			return null;
		}
		return methodStatementMetadata.getResultHandler().tranFromObject((ResultSet) resultSet);
	}


	/**
	 * 设置条件
	 * @param sqlWrapper sql包装类
	 * @param args 参数
	 * @param preparedStatement 指令对象
	 * @param methodStatementMetadata method指令元数据
	 */
	protected void addParameter(SqlWrapper sqlWrapper, Object[] args, PreparedStatement preparedStatement , MethodStatementMetadata methodStatementMetadata) throws SQLException {
		List<String> paramNames = sqlWrapper.getParamNames();
		List<ParamMetadata> paramMetadataList = methodStatementMetadata.getParamMetadataList();
		Map<String , Object> paramMap = new HashMap<>();
		for (int i = 0 ; i < paramMetadataList.size() ; i ++){
			ParamMetadata paramMetadata = paramMetadataList.get(i);
			String firstName = paramMetadata.getName();
			Object arg = args[i];
			//如果是常规类型 如int Integer等
			if (paramMetadata.isGeneric() && paramNames.contains(firstName)){
				paramMap.put(firstName , arg);
			}
			// map
			else if (paramMetadata.isMap()){
				for (Map.Entry<String , Object> entry : ((Map<String , Object>)arg).entrySet()){
					String paramName = firstName + entry.getKey();
					if (paramNames.contains(paramName)) {
						paramMap.put(firstName + entry.getKey(), entry.getValue());
					}
				}
			}
			//entity
			else {
				List<Field> fields = paramMetadata.getFields();
				for (Field field : fields){
					String paramName = firstName + field.getName();
					if (paramNames.contains(paramName)){
						field.setAccessible(true);
						paramMap.put(paramName , ReflectionUtils.get(field , arg));
					}
				}
			}
		}
		//设置参数
		for (int i =0 ; i < paramNames.size() ; i ++){
			preparedStatement.setObject(i +1, paramMap.get(paramNames.get(i)));
		}
		logger.log(Level.INFO , "sql:" + sqlWrapper.getSql() + "\n");
		logger.log(Level.INFO , "params:" + paramNames.stream().collect(Collectors.joining(",")) +"\n");

	}
}
