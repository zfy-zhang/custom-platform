package org.elisha.mybatis.framerwork.core.sqlnode;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/8
 * @Modify
 * @since
 */
public interface SqlNode {
    void apply(DynamicContext context);
}
