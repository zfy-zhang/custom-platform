package org.elisha.mybatis.framerwork.core.sqlsource;

import org.elisha.mybatis.framerwork.core.parser.SqlSourceParser;
import org.elisha.mybatis.framerwork.core.sqlnode.DynamicContext;
import org.elisha.mybatis.framerwork.core.sqlnode.SqlNode;

/**
 * @Description: 用于封装和解析不带有 #{} 或者动态标签的一些信息
 *              SELECT * FROM user WHERE name = #{name}
 *              解析后：只需要解析一次就可以得到以下的 SQL 语句
 *              SELECT * FROM user WHERE name = ？
 *              解析时机：
 *              1、构造的时候进行 SQL 解析，只会解析一次
 *              2、每次调用 DynamicSqlSource.getBoundSql() 的时候进行解析
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/8
 * @Modify
 * @since
 */
public class RawSqlSource implements SqlSource {

    private SqlSource staticSqlSource;

    public RawSqlSource(SqlNode rootSqlNode) {
        // 1、解析 SqlNode 中的所有节点信息，最终组成一条SQL语句
        DynamicContext context = new DynamicContext(rootSqlNode);
        rootSqlNode.apply(context);
        // 2、解析 #{}
        String sqlText = context.getSql();

        SqlSourceParser sqlSourceParser = new SqlSourceParser();
        staticSqlSource = sqlSourceParser.parserSqlSource(sqlText);
    }

    @Override
    public BoundSql getBoundSql(Object param) {
        return staticSqlSource.getBoundSql(param);
    }
}
