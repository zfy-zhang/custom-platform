package org.elisha.orm;

import org.apache.commons.lang.ArrayUtils;
import org.elisha.orm.annotation.*;
import org.elisha.orm.base.proxy.ProxyFactory;
import org.elisha.orm.base.utiil.ReflectionUtils;
import org.elisha.orm.matedata.MethodStatementMetadata;
import org.elisha.orm.matedata.ParamMetadata;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;


/**
 * @Description: mapper构建器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class MapperBuild implements InvocationHandler {

	private static DataSource DATA_SOURCE;

	private static  FieldMappingHandler CONVERTER = new HumpFieldMappingHandler();


	private Class<?> targetClass;


	static {
		ServiceLoader<FieldMappingHandler> serviceLoader = ServiceLoader.load(FieldMappingHandler.class);
		Iterator<FieldMappingHandler> iterator = serviceLoader.iterator();
		if (iterator.hasNext()){
			CONVERTER = iterator.next();
		}
	}

	private Map<Method , MethodStatementMetadata> statementMetadataMap = new HashMap<>();

	public static <T>T build(Class<T> tClass) throws ClassNotFoundException {
		return ProxyFactory.createProxy(new MapperBuild(tClass) , tClass);
	}



	public MapperBuild(Class<?> targetClass) throws ClassNotFoundException {
		this.targetClass = targetClass;
		initMetadata();
	}

	/**
	 * 初始化数据源
	 * @param dataSource
	 */
	public static void initDataSource(DataSource dataSource){
		DATA_SOURCE = dataSource;
	}


	/**
	 * 初始化元数据
	 */
	private void initMetadata() throws ClassNotFoundException {
		//获取所有方法
		List<Method> methods = ReflectionUtils.getMethods(this.targetClass, Object.class);
		//初始化元数据
		for (Method method : methods){
			if (isStatementMethod(method)){
				SqlStatement sqlStatement = ReflectionUtils.getAnnotation(method, SqlStatement.class);
				MethodStatementMetadata methodStatementMetadata = new MethodStatementMetadata();
				methodStatementMetadata.setMethod(method);
				methodStatementMetadata.setStatementType(sqlStatement.value());
				String sql = null;
				Annotation[] annotations = method.getAnnotations();
				if (!ArrayUtils.isEmpty(annotations)){
					for (Annotation annotation : annotations){
						if (annotation.annotationType().isAssignableFrom(Write.class)){
							sql = ((Write)annotation).value();
						}else if (annotation.annotationType().isAssignableFrom(Read.class)){
							sql = ((Read)annotation).value();
						}else if (annotation.annotationType().isAssignableFrom(Metadata.class)){
							sql = ((Metadata)annotation).value();
						}
					}
				}
				methodStatementMetadata.setSql(sql);
				Annotation[][] parameterAnnotations = method.getParameterAnnotations();
				Class<?>[] parameterTypes = method.getParameterTypes();
				List<ParamMetadata> paramMetadataList = new ArrayList<>();
				for (int i =0 , length = parameterTypes.length; i < length ; i ++){
					Annotation[] parameterAnnotation = parameterAnnotations[i];
					String paramName = "";
					for (Annotation annotation : parameterAnnotation){
						if (annotation.annotationType().isAssignableFrom(Param.class)){
							paramName = ((Param)annotation).value();
							break;
						}
					}
					Class<?> parameterType = parameterTypes[i];
					boolean isMap = parameterType == Map.class;
					List<Field> fields = null;
					if (!ReflectionUtils.isGeneralType(parameterType)){
						fields = ReflectionUtils.getFields(parameterType , Object.class);
					}
					ParamMetadata paramMetadata = new ParamMetadata( paramName , fields ,isMap);
					paramMetadataList.add(paramMetadata);

				}
				methodStatementMetadata.setParamMetadataList(paramMetadataList);
				ResultMetadata resultMetadata = new ResultMetadata(method.getGenericReturnType(), CONVERTER);
				//一般类型
				if (resultMetadata.isGenericElement()){
					methodStatementMetadata.setResultHandler(new SimpleResultHandler(resultMetadata));
				}
				//复杂类型
				else{
					methodStatementMetadata.setResultHandler(new ComplexResultHandler(resultMetadata));
				}
				statementMetadataMap.putIfAbsent(method , methodStatementMetadata);
			}
		}
	}

	/**
	 * 是否是指令方法
	 * @param method
	 * @return
	 */
	private boolean isStatementMethod(Method method) {
		return ReflectionUtils.isAnnotation(method , SqlStatement.class);
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try ( Connection connection = DATA_SOURCE.getConnection()){
			MethodStatementMetadata methodStatementMetadata = statementMetadataMap.get(method);
			if (methodStatementMetadata == null) {
				return method.invoke(proxy, args);
			}
			Session session = SessionFactory.getSession(methodStatementMetadata, connection);
			return session.execute(args);
		}
	}
}
