package org.elisha.orm;


import org.elisha.orm.matedata.MethodStatementMetadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description: 预编译指令绘话
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class PreparedStatementSession extends PreparedStatementSupport implements Session {

	private final static Logger logger = Logger.getLogger("");


	private Connection connection;


	private MethodStatementMetadata methodStatementMetadata;


	private static final SqlResolve SQL_RESOLVE = new PlaceholderSqlResolve();

	private SqlResolve sqlResolve = SQL_RESOLVE;



	public PreparedStatementSession(Connection connection , MethodStatementMetadata methodStatementMetadata) throws SQLException {
		this.connection = connection;
		this.methodStatementMetadata  = methodStatementMetadata;
	}




	public ResultSet query(Object[] args) throws Throwable {
		SqlWrapper sqlWrapper = sqlResolve.doResolve(methodStatementMetadata.getSql());
		PreparedStatement preparedStatement = connection.prepareStatement(sqlWrapper.getSql());
		addParameter(sqlWrapper , args , preparedStatement , methodStatementMetadata);
		return preparedStatement.executeQuery();
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}


	@Override
	public int update(Object[] args) throws Throwable{
		SqlWrapper sqlWrapper = sqlResolve.doResolve(methodStatementMetadata.getSql());
		PreparedStatement preparedStatement = connection.prepareStatement(sqlWrapper.getSql());
		addParameter(sqlWrapper , args , preparedStatement , methodStatementMetadata);
		return preparedStatement.executeUpdate();
	}


	private boolean doExecute() throws SQLException {
		logger.log(Level.INFO , methodStatementMetadata.getSql());
		return connection.prepareStatement(methodStatementMetadata.getSql()).execute();
	}

	@Override
	public Object execute(Object[] args) throws Throwable {
		Object resultSet = null;
		MethodStatementMetadata.StatementType statementType = methodStatementMetadata.getStatementType();
		if (statementType == MethodStatementMetadata.StatementType.DLL_CUD){
			resultSet = update(args);
		}else if (statementType == MethodStatementMetadata.StatementType.DLL_R){
			resultSet = query(args);
		}else {
			resultSet = doExecute();
		}
		return parseResult(resultSet , methodStatementMetadata);
	}


}
