package org.elisha.web.mvc.proxy;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
@FunctionalInterface
public interface Advice {

	Object invoke(MethodProcessor methodProcessor) throws Throwable;
}
