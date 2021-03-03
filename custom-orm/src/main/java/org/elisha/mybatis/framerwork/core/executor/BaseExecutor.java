package org.elisha.mybatis.framerwork.core.executor;

import org.elisha.mybatis.framerwork.core.cache.PerpetualCache;
import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.config.MappedStatement;
import org.elisha.mybatis.framerwork.core.sqlsource.BoundSql;
import org.elisha.mybatis.framerwork.core.sqlsource.SqlSource;

import java.util.List;

/**
 * @Description: 抽象出来的一级缓存的处理逻辑
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public abstract class BaseExecutor implements Executor {

    private PerpetualCache perpetualCache = new PerpetualCache();

    @Override
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object param) {
        // 先从一级缓存中获取数据
        SqlSource sqlSource = mappedStatement.getSqlSource();
        BoundSql boundSql = sqlSource.getBoundSql(param);

        String cacheKey = createCacheKey(boundSql);
        List<T> list = (List<T>) perpetualCache.get(cacheKey);
        // 没有在查询数据库
        if (list == null) {
            list = queryFromDataBase(configuration, mappedStatement, boundSql, param);
        }
        return list;
    }

    protected abstract <T> List<T> queryFromDataBase(Configuration configuration, MappedStatement mappedStatement, BoundSql boundSql, Object param);


    protected String createCacheKey(BoundSql boundSql){
        return boundSql.getSql();
    }
}
