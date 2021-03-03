package org.elisha.orm;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description: 抽象的结果处理器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public abstract class AbstractResultHandler  implements ResultHandler {

	private static final Logger logger = Logger.getLogger("");


	protected ResultMetadata resultMetadata;

	private static final Map<Class, ThrowableCallback<ResultSet, Object, Object>> DATABASE_VALUE_HANDLERS = new HashMap<>();


	static {

		//int
		ThrowableCallback<ResultSet, Object, Object> intCall = (resultSet, name) -> {
			if (name instanceof String) {
				return resultSet.getInt((String) name);
			} else {
				return resultSet.getInt((Integer) name);
			}
		};
		DATABASE_VALUE_HANDLERS.put(Integer.class, intCall);
		DATABASE_VALUE_HANDLERS.put(int.class, intCall);
		//long
		ThrowableCallback<ResultSet, Object, Object> longCall = (resultSet, name) -> {
			if (name instanceof String) {
				return resultSet.getLong((String) name);
			} else {
				return resultSet.getLong((Integer) name);
			}
		};
		DATABASE_VALUE_HANDLERS.put(Long.class, longCall);
		DATABASE_VALUE_HANDLERS.put(long.class, longCall);
		//short
		ThrowableCallback<ResultSet, Object, Object> shortCall = (resultSet, name) -> {
			if (name instanceof String) {
				return resultSet.getShort((String) name);
			} else {
				return resultSet.getShort((Integer) name);
			}
		};
		DATABASE_VALUE_HANDLERS.put(Short.class, shortCall);
		DATABASE_VALUE_HANDLERS.put(short.class, shortCall);
		// byte
		ThrowableCallback<ResultSet, Object, Object> byteCall = (resultSet, name) -> {
			if (name instanceof String) {
				return resultSet.getByte((String) name);
			} else {
				return resultSet.getByte((Integer) name);
			}
		};
		DATABASE_VALUE_HANDLERS.put(byte.class, byteCall);
		DATABASE_VALUE_HANDLERS.put(Byte.class, byteCall);
		//string
		ThrowableCallback<ResultSet, Object, Object> stringCall = (resultSet, name) -> {
			if (name instanceof String) {
				return resultSet.getString((String) name);
			} else {
				return resultSet.getString((Integer) name);
			}
		};

		DATABASE_VALUE_HANDLERS.put(String.class, stringCall);
		//double
		ThrowableCallback<ResultSet, Object, Object> doubleCall = (resultSet, name) -> {
			if (name instanceof String) {
				return resultSet.getDouble((String) name);
			} else {
				return resultSet.getDouble((Integer) name);
			}
		};
		DATABASE_VALUE_HANDLERS.put(Double.class, doubleCall);
		DATABASE_VALUE_HANDLERS.put(double.class, doubleCall);
		//float
		ThrowableCallback<ResultSet, Object, Object> floatCall = (resultSet, name) -> {
			if (name instanceof String) {
				return resultSet.getFloat((String) name);
			} else {
				return resultSet.getFloat((Integer) name);
			}
		};
		DATABASE_VALUE_HANDLERS.put(Float.class, floatCall);
		DATABASE_VALUE_HANDLERS.put(float.class, floatCall);
		//boolean
		ThrowableCallback<ResultSet, Object, Object> booleanCall = (resultSet, name) -> {
			if (name instanceof String) {
				return resultSet.getBoolean((String) name);
			} else {
				return resultSet.getBoolean((Integer) name);
			}
		};
		DATABASE_VALUE_HANDLERS.put(Boolean.class, booleanCall);
		DATABASE_VALUE_HANDLERS.put(boolean.class, booleanCall);
		// Object
		ThrowableCallback<ResultSet, Object, Object> objectCall = (resultSet, name) -> {
			if (name instanceof String) {
				return resultSet.getBoolean((String) name);
			} else {
				return resultSet.getBoolean((Integer) name);
			}
		};
		DATABASE_VALUE_HANDLERS.put(Object.class, objectCall);
	}



	public AbstractResultHandler(ResultMetadata resultMetadata){
		this.resultMetadata = resultMetadata;
	}

	/**
	 * 校验结果数量
	 * @param elementCount 查询到的元素个数
	 * @param wrapperObject 包装类
	 * @throws SQLException
	 */
	protected void  checkResultCount(int elementCount , Collection<Object> wrapperObject) throws SQLException {
		//校验响应结果类型和数量和实际数量是否一致
		if (elementCount > 1 && wrapperObject == null){
			throw new SQLException("你预期要查询一个元素，但实际查出多个元素");
		}
	}


	/**
	 * 获取结果集的值
	 *
	 * @param clazz      值的类型
	 * @param resultSet  结果集
	 * @param  c 可以是索引值也可以是列名称
	 * @return
	 */
	public Object getValue(Class clazz, ResultSet resultSet, Object c) throws Throwable {
		ThrowableCallback<ResultSet, Object, Object> callback = DATABASE_VALUE_HANDLERS.get(clazz);
		if (callback == null) {
			logger.log(Level.SEVERE, String.format("不支持的数据类型%s", clazz.getTypeName()));
			return null;
		}
		return callback.call(resultSet, c);
	}


	public Object tranFromObject(ResultSet resultSet) {
		try {
			return tranFromObject0(resultSet);
		} catch (Throwable e) {
			throw new IllegalStateException(e);
		}
	}


	@Override
	public ResultMetadata getResultMetadata() {
		return this.resultMetadata;
	}

	protected abstract Object tranFromObject0(ResultSet resultSet) throws Throwable;


}
