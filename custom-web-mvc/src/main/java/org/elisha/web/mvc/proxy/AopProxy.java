package org.elisha.web.mvc.proxy;


import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public interface AopProxy {

	Object getTarget();


	Object getProxy();


	List<Advice> getAdvice();

}
