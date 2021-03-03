package org.elisha.mybatis.framerwork.core.handler;

import org.elisha.mybatis.framerwork.core.config.MappedStatement;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class DefaultResultSetHandler implements ResultSetHandler {
    @Override
    public <T> List<T> handlerResult(Statement statement, ResultSet resultSet, MappedStatement mappedStatement) throws Exception {
        List<Object> results = new ArrayList<>();
        if (statement instanceof PreparedStatement) {
            PreparedStatement preparedStatement = (PreparedStatement) statement;
            Object result = null;
            String resultType = mappedStatement.getResultType();
            Class clazz = Class.forName(resultType);
            while (resultSet.next()) {
                Constructor constructor = clazz.getConstructor();
                result = constructor.newInstance();
                ResultSetMetaData metaData = resultSet.getMetaData();
                // 结果集中列的数量
                int columnSize = metaData.getColumnCount();
                for (int i = 0; i < columnSize ; i++) {
                    String columnName = metaData.getColumnName(i + 1);

                    // 给对象属性赋值
                    // 查询结果中的列名和要映射的对象的属性名必须一致
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(result, resultSet.getObject(i + 1));
                }
                results.add(result);
            }
        }
        return (List<T>) results;
    }
}
