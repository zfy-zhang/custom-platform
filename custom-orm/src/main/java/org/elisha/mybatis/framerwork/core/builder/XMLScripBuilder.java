package org.elisha.mybatis.framerwork.core.builder;

import org.elisha.mybatis.framerwork.core.sqlnode.*;
import org.elisha.mybatis.framerwork.core.sqlsource.DynamicSqlSource;
import org.elisha.mybatis.framerwork.core.sqlsource.RawSqlSource;
import org.elisha.mybatis.framerwork.core.sqlsource.SqlSource;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class XMLScripBuilder {

    private boolean isDynamic = false;

    /**
     *
     * @param selectElement
     * @return
     */
    public SqlSource parseScriptSqlNode(Element selectElement) {
        // 1、解析动态标签，获取 SqlNode信息
        SqlNode mixedSqlNode = parseDynamicTags(selectElement);
        // 2、封装 SqlSource
        SqlSource sqlSource = null;

        if (isDynamic) {
            sqlSource = new DynamicSqlSource(mixedSqlNode);
        } else {
            sqlSource = new RawSqlSource(mixedSqlNode);
        }
        return sqlSource;
    }

    /**
     *
     * @param selectElement
     * @return
     */
    private SqlNode parseDynamicTags(Element selectElement) {
        List<SqlNode> sqlNodes = new ArrayList<>();
        int nodeCount = selectElement.nodeCount();
        for (int i = 0; i < nodeCount; i++) {
            Node node = selectElement.node(i);
            if (node instanceof Text) {
                String text = node.getText().trim();
                if (text.equals("")) {
                    continue;
                }
                TextSqlNode textSqlNode = new TextSqlNode(text);
                if (textSqlNode.isDynamic()) {
                    isDynamic = true;
                    sqlNodes.add(textSqlNode);
                } else {
                    sqlNodes.add(new StaticTextSqlNode(text));
                }
            } else if (node instanceof Element) {
                // 此处是动态标签的处理逻辑
                isDynamic = true;
                Element element = (Element) node;
                String name = element.getName();
                if ("if".equals(name)) {
                    String test = element.attributeValue("test");
                    SqlNode mixedSqlNode = parseDynamicTags(element);
                    SqlNode ifSqlNode = new IfSqlNode(test, mixedSqlNode);
                    sqlNodes.add(ifSqlNode);
                } else if ("where".equals(name)) {

                }
            } else {
                // .....
            }
        }
        return new MixedSqlNode(sqlNodes);
    }
}
