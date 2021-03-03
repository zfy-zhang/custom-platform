package org.elisha.mybatis.framerwork.core.executor;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.config.MappedStatement;

import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class CachingExecutor implements Executor {

    private Executor delegate;

    public CachingExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object param) {

        // TODO 二级缓存的执行器
        // 从 ms 中获取它的二级缓存对象

        // 如果没有配置二级缓存， 则调正在的执行器
        return delegate.query(configuration, mappedStatement, param);
    }
}
