package org.elisha.mybatis.framerwork.core.parser;

import org.elisha.mybatis.framerwork.core.sqlsource.SqlSource;
import org.elisha.mybatis.framerwork.core.sqlsource.StaticSqlSource;
import org.elisha.mybatis.framerwork.core.utils.GenericTokenParser;
import org.elisha.mybatis.framerwork.core.utils.ParameterMappingTokenHandler;


/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/9
 * @Modify
 * @since
 */
public class SqlSourceParser {

    public SqlSource parserSqlSource(String sqlText) {
        // 分词处理器，该如何处理 #{} 中的内容
        // 思路：将 #{} 中的内容封装成 ParameterMapping 对象，并放入 List 集合
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
        // 通用分词解析器
        // openToken：
        // closeToken
        // tokenHandler：被分出来的词该怎么处理
        GenericTokenParser tokenParser = new GenericTokenParser("#{", "}", tokenHandler);

        // 使用通用分词解析器针对指定文本进行解析，解析之后得到JDBC可以执行的SQL语句
        String sql = tokenParser.parse(sqlText);

        // 3、将解析出来的SQL语句和参数列表封装到StaticSqlSource中
        // SqlSource staticSqlSource = new StaticSqlSource(sql, tokenHandler.getParameterMappings());
        return new StaticSqlSource(sql, tokenHandler.getParameterMappings());
    }
}
