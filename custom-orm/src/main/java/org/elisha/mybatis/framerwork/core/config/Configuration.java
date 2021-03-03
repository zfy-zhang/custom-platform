package org.elisha.mybatis.framerwork.core.config;


import org.elisha.mybatis.framerwork.core.executor.CachingExecutor;
import org.elisha.mybatis.framerwork.core.executor.Executor;
import org.elisha.mybatis.framerwork.core.executor.SimpleExecutor;
import org.elisha.mybatis.framerwork.core.handler.*;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 存储全局配置文件和映射文件数据
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/7
 * @Modify
 * @since
 */
public class Configuration {

    private DataSource dataSource;

    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();

    private boolean useCache = true;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MappedStatement getMappedStatementById(String statementId) {
        return mappedStatementMap.get(statementId);
    }

    public void addMappedStatementMap(String statementId, MappedStatement mappedStatement) {
        this.mappedStatementMap.put(statementId, mappedStatement);
    }

    public Executor newExecutor(String executorType) {
        // r如果没有指定 executorType，则默认为 simple
        executorType = executorType == null || executorType.equals("") ? "simple" : executorType;

        Executor executor = null;
        if ("simple".equals(executorType)) {
            executor = new SimpleExecutor(); // 创建真正干活的执行器
        }
        // 装饰模式
        if (useCache) {
            // 创建二级缓存处理功能的执行器，但是只倾其还是要继续执行下去，还是要调用SimpleExecutor
            executor = new CachingExecutor(executor);
        }
        return executor;
    }

    public StatementHandler newStatementHandler(String statementType) {
        RouterStatementHandler routerStatementHandler = new RouterStatementHandler(statementType, this);
        return routerStatementHandler;
    }

    public ParameterHandler newParameterHandler() {
        return new DefaultParameterHandler();
    }

    public ResultSetHandler newResultSetHandler() {
        return new DefaultResultSetHandler();
    }
}
