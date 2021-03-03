package org.elisha.mybatis.framerwork.core.sqlnode;

/**
 * @Description: 存储不带有 ${} 的 SQL 文本信息
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/9
 * @Modify
 * @since
 */
public class StaticTextSqlNode implements SqlNode {

    private String sqlText;

    public StaticTextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }

    @Override
    public void apply(DynamicContext context) {
        context.appendSql(sqlText);
    }
}
