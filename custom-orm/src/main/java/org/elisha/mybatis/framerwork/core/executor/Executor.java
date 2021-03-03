package org.elisha.mybatis.framerwork.core.executor;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.config.MappedStatement;

import java.util.List;

/**
 * @Description: 执行器
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public interface Executor {

    /**
     *
     * @param configuration
     * @param mappedStatement
     * @param param
     * @param <T>
     * @return
     */
    <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object param);
}
