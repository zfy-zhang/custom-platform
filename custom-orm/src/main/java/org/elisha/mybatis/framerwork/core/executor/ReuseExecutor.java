package org.elisha.mybatis.framerwork.core.executor;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.config.MappedStatement;
import org.elisha.mybatis.framerwork.core.sqlsource.BoundSql;

import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class ReuseExecutor extends BaseExecutor {


    @Override
    protected <T> List<T> queryFromDataBase(Configuration configuration, MappedStatement mappedStatement, BoundSql boundSql, Object param) {
        return null;
    }
}
