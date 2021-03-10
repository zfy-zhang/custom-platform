package org.elisha.web.mvc.jndi;


import org.elisha.orm.MapperBuild;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class MapperBeanFactory implements ObjectFactory {



	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
		Reference reference = (Reference) obj;
		return MapperBuild.build(Class.forName(reference.getClassName()));
	}
}
