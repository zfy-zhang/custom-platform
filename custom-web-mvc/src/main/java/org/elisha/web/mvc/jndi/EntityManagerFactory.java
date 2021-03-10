package org.elisha.web.mvc.jndi;


import org.elisha.web.mvc.context.ApplicationContext;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import javax.persistence.Persistence;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * @Description: jpa 实体管理对象工厂
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
@Deprecated
public class EntityManagerFactory implements ObjectFactory {

	private final static String PERSISTENCE_UNIT_NAME = "persistenceUnitName";

	private final static String PROPERTIES_LOCATION = "propertiesLocation";

	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
		Reference reference = (Reference) obj;
		RefAddr persistenceUnitNameRefAddr = reference.get(PERSISTENCE_UNIT_NAME);
		RefAddr propertiesLocationRefAddr = reference.get(PROPERTIES_LOCATION);
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader.getResourceAsStream(propertiesLocationRefAddr.getContent().toString());
		Properties properties = new Properties();
		properties.load(resourceAsStream);
		//处理引用
		Enumeration<?> enumeration = properties.propertyNames();
		while (enumeration.hasMoreElements()){
			Object key = enumeration.nextElement();
			Object value = properties.get(key);
			if (value.toString().startsWith("@")){
				properties.put(key , ApplicationContext.lookup(value.toString().substring(1)).getBean());
			}
		}
		javax.persistence.EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitNameRefAddr.getContent().toString(), properties);
		return entityManagerFactory.createEntityManager();
	}
}
