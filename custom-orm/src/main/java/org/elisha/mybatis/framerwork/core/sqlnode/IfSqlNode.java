package org.elisha.mybatis.framerwork.core.sqlnode;

import org.elisha.mybatis.framerwork.core.utils.OgnlUtils;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/9
 * @Modify
 * @since
 */
public class IfSqlNode implements SqlNode {

    private String text;

    private SqlNode mixedSqlNode;

    public IfSqlNode(String text, SqlNode mixedSqlNode) {
        this.text = text;
        this.mixedSqlNode = mixedSqlNode;
    }

    @Override
    public void apply(DynamicContext context) {
        Object parameter = context.getBindings().get("_parameter");
        boolean aBoolean = OgnlUtils.evaluateBoolean(text, parameter);
        if (aBoolean) {
            mixedSqlNode.apply(context);
        }
    }
}
