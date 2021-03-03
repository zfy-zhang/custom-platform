package org.elisha.orm;

/**
 * @Description: sql解析器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public interface SqlResolve  {

	SqlWrapper doResolve(String sql);
}
