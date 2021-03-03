package org.elisha.mybatis.framerwork.core.handler;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.config.MappedStatement;
import org.elisha.mybatis.framerwork.core.sqlsource.BoundSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class PreparedStatementHandler implements StatementHandler {

    private ParameterHandler parameterHandler;
    private ResultSetHandler resultSetHandler;

    public PreparedStatementHandler(Configuration configuration) {
        this.parameterHandler = configuration.newParameterHandler();
        this.resultSetHandler = configuration.newResultSetHandler();
    }

    @Override
    public Statement prepare(Connection connection, String sql) throws Exception {
        return connection.prepareStatement(sql);
    }

    @Override
    public void parameterize(Statement statement, Object param, BoundSql boundSql) throws Exception {

        PreparedStatement preparedStatement = (PreparedStatement) statement;
        // 真正进行参数处理的是 PreparedStatementHandler
        parameterHandler.setParameter(preparedStatement, param, boundSql);

    }

    @Override
    public <T> List<T> executeQuery(Statement statement, MappedStatement mappedStatement) throws Exception {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSetHandler.handlerResult(statement, resultSet, mappedStatement);
    }
}
