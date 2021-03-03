package org.elisha.mybatis.framerwork.core.handler;

import org.elisha.mybatis.framerwork.core.sqlsource.BoundSql;
import org.elisha.mybatis.framerwork.core.sqlsource.ParameterMapping;
import org.elisha.mybatis.framerwork.core.utils.SimpleTypeRegistry;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class DefaultParameterHandler implements ParameterHandler {

    @Override
    public void setParameter(Statement statement, Object param, BoundSql boundSql) throws Exception {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        if (SimpleTypeRegistry.isSimpleType(param.getClass())) {
            preparedStatement.setObject(1, param);
        } else if (param instanceof Map) {
            Map map = (Map) param;

            // 需要进行SQL解析之后，才会处理该部分内容，需要解析#{}才会得到参数列表
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String name = parameterMapping.getName();
                Object value = map.get(name);
                preparedStatement.setObject(i + 1, value);
            }
        }
    }
}
