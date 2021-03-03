package org.elisha.mybatis.framerwork.core.sqlnode;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 1、用于拼接 {@link SqlNode} 解析完成之后的SQL片段
 *               2、用于传递解析 {@link SqlNode} 时需要的参数信息
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/8
 * @Modify
 * @since
 */
public class DynamicContext {

    private StringBuffer sb = new StringBuffer();
    private Map<String, Object> bindings = new HashMap<>();

    /**
     * 通过构造方法，注入参数信息
     * @param param
     */
    public DynamicContext(Object param) {
        this.bindings.put("_parameter", param);
    }

    /**
     * 获取解析后的 SQL 语句
     * @return
     */
    public String getSql() {
        return sb.toString();
    }

    /**
     * 拼接 SQL 语句
     * @param sqlText
     */
    public void appendSql(String sqlText) {
        this.sb.append(sqlText);
        this.sb.append(" ");
    }

    /**
     * 获取绑定参数信息
     * @return
     */
    public Map<String, Object> getBindings() {
        return bindings;
    }
}
