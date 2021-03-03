package org.elisha.mybatis.framerwork.core.sqlsource;

import java.util.List;

/**
 * @Description: 用于存储 {@link DynamicSqlSource} 和 {@link RawSqlSource} 解析之后的结果
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/8
 * @Modify
 * @since
 */
public class StaticSqlSource implements SqlSource {

    private String sql;
    private List<ParameterMapping> parameterMappings;

    @Override
    public BoundSql getBoundSql(Object param) {
        return new BoundSql(sql, parameterMappings);
    }

    public StaticSqlSource(String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }
}
