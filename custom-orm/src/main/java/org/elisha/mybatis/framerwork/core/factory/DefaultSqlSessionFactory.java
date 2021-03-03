package org.elisha.mybatis.framerwork.core.factory;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.executor.Executor;
import org.elisha.mybatis.framerwork.core.sqlsession.DefaultSqlSession;
import org.elisha.mybatis.framerwork.core.sqlsession.SqlSession;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        // 获取 Executor：一种是默认的 simple，一种是全局的，一种是指定的ExecutorType
        Executor executor = configuration.newExecutor(null);
        return new DefaultSqlSession(configuration, executor);
    }
}
