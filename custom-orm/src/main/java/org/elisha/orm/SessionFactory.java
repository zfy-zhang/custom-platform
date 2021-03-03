package org.elisha.orm;

import org.elisha.orm.matedata.MethodStatementMetadata;

import java.sql.Connection;
import java.sql.SQLException;

/**
* 回话工厂  
* @author : KangNing Hu
*/
/**
 * @Description: 抽象的结果处理器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class SessionFactory {



	public static Session getSession(MethodStatementMetadata methodStatementMetadata  , Connection connection) throws SQLException {
		return new PreparedStatementSession(connection , methodStatementMetadata);
	}

}
