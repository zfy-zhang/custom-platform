package org.elisha.mybatis.framerwork.core.handler;

import org.elisha.mybatis.framerwork.core.sqlsource.BoundSql;

import java.sql.Statement;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public interface ParameterHandler {
    void setParameter(Statement statement, Object param, BoundSql boundSql) throws Exception;
}
