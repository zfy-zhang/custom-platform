package org.elisha.mybatis.framerwork.core.sqlsource;


import org.elisha.mybatis.framerwork.core.parser.SqlSourceParser;
import org.elisha.mybatis.framerwork.core.sqlnode.DynamicContext;
import org.elisha.mybatis.framerwork.core.sqlnode.SqlNode;

/**
 * @Description: 用于封装和解析带有 #{} 或者动态标签的一些信息
 *              SELECT * FROM user WHERE name = ${name}
 *              根据每一次参数的不同得到的sql不同：
 *              SELECT * FROM user WHERE name = wangwu
 *              SELECT * FROM user WHERE name = zhaoliu
 *              解析时机：
 *              1、构造的时候进行 SQL 解析，只会解析一次
 *              2、每次调用 DynamicSqlSource.getBoundSql() 的时候进行解析
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/8
 * @Modify
 * @since
 */
public class DynamicSqlSource implements SqlSource {

    private SqlNode rootSqlNode;

    public DynamicSqlSource(SqlNode rootSqlNode) {
        this.rootSqlNode = rootSqlNode;
    }

    /**
     * 每一次都需要重新解析sql语句
     * @param param
     * @return
     */
    @Override
    public BoundSql getBoundSql(Object param) {
        // 1、解析 SqlNode 中的所有节点信息，最终组成一条SQL语句（解析${}和动sql）
        DynamicContext context = new DynamicContext(param);
        rootSqlNode.apply(context);
        // 2、解析 #{}
        String sqlText = context.getSql();
        SqlSourceParser sqlSourceParser = new SqlSourceParser();
        SqlSource sqlSource = sqlSourceParser.parserSqlSource(sqlText);
        return sqlSource.getBoundSql(param);
    }
}
