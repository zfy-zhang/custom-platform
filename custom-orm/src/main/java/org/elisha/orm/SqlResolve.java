package org.elisha.orm;

/**
* sql解析器  
* @author : KangNing Hu
*/
/**
 * @Description: 抽象的结果处理器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public interface SqlResolve  {

	SqlWrapper doResolve(String sql);
}
