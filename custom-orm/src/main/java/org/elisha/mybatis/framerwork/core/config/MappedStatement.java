package org.elisha.mybatis.framerwork.core.config;

import org.elisha.mybatis.framerwork.core.sqlsource.SqlSource;

/**
 * @Description: 存储映射文件中的 crud 标签的内容
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/7
 * @Modify
 * @since
 */
public class MappedStatement {

    private String id;

    private String resultType;

    private String statementType;

    private SqlSource sqlSource;

    public MappedStatement(String id, String resultType, String statementType, SqlSource sqlSource) {
        this.id = id;
        this.resultType = resultType;
        this.statementType = statementType;
        this.sqlSource = sqlSource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public void setSqlSource(SqlSource sqlSource) {
        this.sqlSource = sqlSource;
    }
}
