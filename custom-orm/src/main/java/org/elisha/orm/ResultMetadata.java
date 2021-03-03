package org.elisha.orm;

import org.apache.commons.lang.StringUtils;
import org.elisha.orm.base.utiil.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.*;

/**
* 响应结果元信息  
* @author : KangNing Hu
*/
/**
 * @Description: 抽象的结果处理器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class ResultMetadata {




	private static final  String LIST_NAME = "list";

	private static final String SET_NAME = "set";

	private  FieldMappingHandler fieldMappingHandler;

	private String collectionName;

	private Type runtType;

	private List<Field> fields;

	private boolean isMap;


	public ResultMetadata(Type returnType , FieldMappingHandler fieldMappingHandler) throws ClassNotFoundException {
		this.runtType = returnType;
		this.fieldMappingHandler = fieldMappingHandler;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() throws ClassNotFoundException {
		Class entityType = null;
		if (runtType instanceof ParameterizedType){
			Type rawType = ((ParameterizedType) runtType).getRawType();
			if (rawType == List.class || rawType == Set.class){
				this.collectionName = rawType == List.class ? LIST_NAME : SET_NAME;
				entityType = (Class) ((ParameterizedType) runtType).getActualTypeArguments()[0];
			}
			if (entityType == null){
				entityType = (Class) rawType;
			}
		}else {
			entityType = (Class) runtType;
		}
		if (entityType == Map.class){
			isMap = true;
		}else if (!ReflectionUtils.isGeneralType(entityType)){
			this.fields = ReflectionUtils.getFields(entityType, Object.class);
		}
		this.runtType = entityType;
	}

	/**
	 * 是否是集合类型的返回值
	 * @return
	 */
	public boolean isRuntCollection(){
		return !StringUtils.isEmpty(collectionName);
	}

	/**
	 * 获取包装对象
	 * 1. 如果返回值为List<User> 包装对象为list
	 * 2. 如果返回值为Set<User>包装对象为set
	 * @return
	 */
	public Collection<Object> getWrapperObject(){
		if (isRuntCollection()){
			return LIST_NAME.equals(collectionName) ? new ArrayList<>() : new HashSet<>();
		}
		return null;
	}


	/**
	 * 获取元素类型
	 * 1. 如果返回值为 Interge 类型 则为Interge
	 * 2. 如果返回值为 List<User> 类型 则为User
	 * 3. 如果返回值为 List 类型 则为 Map
	 * 4. 如果返回值为 Map 类型 则为 Map
	 * @return
	 */
	public Object getElementObject(){
		if (isMap){
			return new LinkedHashMap<>();
		}else {
			try {
				return ((Class)runtType).newInstance();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
	}


	/**
	 * 是否为一般的元素
	 * 1. 如果 返回值为 Interge , Long .....则返回true
	 * 2. 如果 返回值为 List<Long> ..... 则放回true
	 * @return
	 */
	public boolean isGenericElement(){
		return ReflectionUtils.isGeneralType((Class) this.runtType);
	}


	/**
	 * 获取元素类型的 字段对象集合
	 * @return
	 */
	public List<Field> getFields() {
		return fields == null ? new ArrayList<>() : fields;
	}

	/**
	 * 获取返回值元素类型
	 * @return
	 */
	public Class getReturnElementType(){
		return (Class) this.runtType;
	}

	/**
	 * 设置元素值
	 * @param resultSet 结果集
	 * @param resultHandler 结果处理器
	 * @param target 待处理的目标对象
	 * @param name 名称 列名 或 属性名
	 */
	public void setValue(ResultSet resultSet , AbstractResultHandler resultHandler ,  Object target , String name ){

		try {
			if (target instanceof Map) {
				((Map) target).put(name, resultHandler.getValue(Object.class, resultSet, name));
			}else if (target instanceof FieldWrapper){
				FieldWrapper fieldWrapper = (FieldWrapper) target;
				fieldWrapper.setValue(resultHandler.getValue(fieldWrapper.getFieldType() , resultSet , fieldMappingHandler.fieldNameToColumnName(name)));
			}
		}catch (Throwable e){
			throw new IllegalStateException(e);
		}
	}
}
