package org.elisha.mybatis.framerwork.core.executor;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.config.MappedStatement;
import org.elisha.mybatis.framerwork.core.handler.StatementHandler;
import org.elisha.mybatis.framerwork.core.sqlsource.BoundSql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 简单执行器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class SimpleExecutor extends BaseExecutor {


    @Override
    protected <T> List<T> queryFromDataBase(Configuration configuration, MappedStatement mappedStatement, BoundSql boundSql, Object param) {

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        List<T> results = new ArrayList<>();

        try {

            // 获取链接
            connection = getConnection(configuration);

            // 根据传入的statementType获取对应的statementHandler类型
            StatementHandler statementHandler = configuration.newStatementHandler(mappedStatement.getStatementType());
            // 获取 StatementType，创建 Statement
            statement = statementHandler.prepare(connection, boundSql.getSql());

            // 设置参数
            statementHandler.parameterize(statement, param, boundSql);

            // 执行 Statement
            results = statementHandler.executeQuery(statement, mappedStatement);
            // 结果映射
            return results;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 释放资源
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private Connection getConnection(Configuration configuration) throws SQLException {
        DataSource dataSource = configuration.getDataSource();
        return dataSource.getConnection();
    }
}
