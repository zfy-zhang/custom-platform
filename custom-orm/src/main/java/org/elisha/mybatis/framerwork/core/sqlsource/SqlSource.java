package org.elisha.mybatis.framerwork.core.sqlsource;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/8
 * @Modify
 * @since
 */
public interface SqlSource {
    /**
     * 解析 ${} 需要的参数
     * @param param
     * @return
     */
    BoundSql getBoundSql(Object param);
}
