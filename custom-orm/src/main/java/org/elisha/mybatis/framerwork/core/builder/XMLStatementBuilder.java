package org.elisha.mybatis.framerwork.core.builder;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.config.MappedStatement;
import org.elisha.mybatis.framerwork.core.sqlsource.SqlSource;
import org.dom4j.Element;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class XMLStatementBuilder {

    private Configuration configuration;
    
    public XMLStatementBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parseStatement(String namespace, Element selectElement) {
        String id = selectElement.attributeValue("id");
        String statementId = namespace + "." +id;
        String resultType = selectElement.attributeValue("resultType");
        String statementType = selectElement.attributeValue("statementType");

        SqlSource sqlSource = createSqlSource(selectElement);
        // 封装 MapperStatement
        MappedStatement mappedStatement = new MappedStatement(statementId, resultType, statementType, sqlSource);
        configuration.addMappedStatementMap(statementId, mappedStatement);
    }

    /**
     *
     * @param selectElement
     * @return
     */
    private SqlSource createSqlSource(Element selectElement) {

        XMLScripBuilder scripBuilder = new XMLScripBuilder();
        SqlSource sqlSource = scripBuilder.parseScriptSqlNode(selectElement);
        return sqlSource;
    }

}
