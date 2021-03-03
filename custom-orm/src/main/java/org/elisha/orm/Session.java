package org.elisha.orm;


import java.sql.Connection;
import java.sql.ResultSet;

/**
* 回话  
* @author : KangNing Hu
*/
/**
 * @Description: 抽象的结果处理器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public interface Session {




	int update(Object[] args) throws Throwable;


	Object execute(Object[] args) throws Throwable;



	ResultSet query(Object[] args) throws Throwable;


	Connection getConnection();


}
