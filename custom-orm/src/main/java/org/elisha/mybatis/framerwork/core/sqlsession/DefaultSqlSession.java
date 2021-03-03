package org.elisha.mybatis.framerwork.core.sqlsession;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.config.MappedStatement;
import org.elisha.mybatis.framerwork.core.executor.Executor;

import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> List<T> selectList(String statementId, Object param) {
        // 获取 MapperStatement
        MappedStatement mappedStatement = configuration.getMappedStatementById(statementId);
        // 调用 Executor 方法
        return executor.query(configuration, mappedStatement, param);
    }

    @Override
    public <T> T selectOne(String statementId, Object param) {
        List<T> list = this.selectList(statementId, param);
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else {
            // TODO 抛出异常
        }
        return null;
    }
}
