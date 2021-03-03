package org.elisha.mybatis.framerwork.core.sqlnode;

import java.util.List;

/**
 * @Description: 存储同级别下的多个 {@link SqlNode} 节点信息，方便统一处理
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/9
 * @Modify
 * @since
 */
public class MixedSqlNode implements SqlNode {

    private List<SqlNode> sqlNodes;

    public MixedSqlNode(List<SqlNode> sqlNodes) {
        this.sqlNodes = sqlNodes;
    }

    @Override
    public void apply(DynamicContext context) {
        for (SqlNode sqlNode : sqlNodes) {
            sqlNode.apply(context);
        }
    }
}
