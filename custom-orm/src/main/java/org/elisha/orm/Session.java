package org.elisha.orm;


import java.sql.Connection;
import java.sql.ResultSet;

/**
 * @Description: 绘话
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
