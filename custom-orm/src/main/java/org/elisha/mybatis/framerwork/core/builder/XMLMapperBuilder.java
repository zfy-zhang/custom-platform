package org.elisha.mybatis.framerwork.core.builder;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.dom4j.Element;

import java.util.List;

/**
 * @Description: 解析映射文件
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 解析映射文件
     * @param rootElement <mapper></mapper>
     */
    public void parseMapper(Element rootElement) {
        String namespace = rootElement.attributeValue("namespace");
        List<Element> selectElements = rootElement.elements("select");
        for (Element selectElement : selectElements) {
            XMLStatementBuilder statementBuilder = new XMLStatementBuilder(configuration);
            statementBuilder.parseStatement(namespace, selectElement);
        }
    }
}
