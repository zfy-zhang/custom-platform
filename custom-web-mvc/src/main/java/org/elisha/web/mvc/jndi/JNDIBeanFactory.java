package org.elisha.web.mvc.jndi;

import org.apache.commons.lang.StringUtils;
import org.elisha.orm.base.utiil.ReflectionUtils;
import org.elisha.web.mvc.context.ApplicationContext;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @Description: JNDI BeanFactory
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class JNDIBeanFactory implements ObjectFactory {


	private static final String REF_PREFIX = "ref-";

	private static final String ATTR_PREFIX = "attr-";

	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
		Reference reference = (Reference) obj;
		Class<?> aClass = Class.forName(reference.getClassName());
		Object o = aClass.newInstance();
		Enumeration<RefAddr> enumeration = reference.getAll();
		List<Field> fields = ReflectionUtils.getFields(aClass, Object.class);
		Map<String, Field> fieldMap = fields.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
		while (enumeration.hasMoreElements()){
			RefAddr refAddr = enumeration.nextElement();
			String type = refAddr.getType();
			boolean isRef = false;
			//如果是内置属性则跳过
			if (!isAttr(type)){
				continue;
			}
			if (type.startsWith(REF_PREFIX)){
				isRef = true;
				type = StringUtils.substringAfter(type , REF_PREFIX);
			}else if (type.startsWith(ATTR_PREFIX)){
				type = StringUtils.substringAfter(type , ATTR_PREFIX);
			}
			Field field = fieldMap.get(type);
			if (field == null){
				throw new IllegalArgumentException(String.format("在类%s中没有字段%s" , reference.getClassName() , type));
			}
			//引用类型
			if (isRef){
				ReflectionUtils.set(field , o , ApplicationContext.lookup((String) refAddr.getContent()).getBean());
			}
			//非引用类型
			else{
				ReflectionUtils.set(field, o, refAddr.getContent());
			}
		}
		return o;
	}


	/**
	 * 是否是属性
	 * @param type
	 * @return
	 */
	private boolean isAttr(String type){
		return type.startsWith(ATTR_PREFIX) || type.startsWith(REF_PREFIX);
	}
}
