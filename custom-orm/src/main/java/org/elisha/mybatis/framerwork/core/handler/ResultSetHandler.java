package org.elisha.mybatis.framerwork.core.handler;

import org.elisha.mybatis.framerwork.core.config.MappedStatement;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public interface ResultSetHandler {
    <T> List<T> handlerResult(Statement statement, ResultSet resultSet, MappedStatement mappedStatement) throws  Exception;
}
