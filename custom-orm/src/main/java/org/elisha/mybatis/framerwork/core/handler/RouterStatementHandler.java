package org.elisha.mybatis.framerwork.core.handler;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.config.MappedStatement;
import org.elisha.mybatis.framerwork.core.sqlsource.BoundSql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class RouterStatementHandler implements StatementHandler {

    private StatementHandler delegate;

    public RouterStatementHandler(String statementType, Configuration configuration) {
        switch (statementType) {
            case "prepared":
                delegate = new PreparedStatementHandler(configuration);
                break;
            case  "callable":
                delegate = new CallableStatementHandler(configuration);
            case "simple":
                delegate = new SimpleStatementHandler(configuration);
            default:
                delegate = new SimpleStatementHandler(configuration);

        }
    }

    @Override
    public Statement prepare(Connection connection, String sql) throws Exception {
        return delegate.prepare(connection, sql);
    }

    @Override
    public void parameterize(Statement statement, Object param, BoundSql boundSql) throws Exception {
        delegate.parameterize(statement, param, boundSql);
    }

    @Override
    public <T> List<T> executeQuery(Statement statement, MappedStatement mappedStatement) throws Exception {
        return delegate.executeQuery(statement, mappedStatement);
    }
}
