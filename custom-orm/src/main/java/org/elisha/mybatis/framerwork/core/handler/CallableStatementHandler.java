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
public class CallableStatementHandler implements StatementHandler {

    public CallableStatementHandler(Configuration configuration) {
    }

    @Override
    public Statement prepare(Connection connection, String sql) throws Exception {
        return null;
    }

    @Override
    public void parameterize(Statement statement, Object param, BoundSql boundSql) throws Exception {

    }

    @Override
    public <T> List<T> executeQuery(Statement statement, MappedStatement mappedStatement) throws Exception {
        return null;
    }
}
