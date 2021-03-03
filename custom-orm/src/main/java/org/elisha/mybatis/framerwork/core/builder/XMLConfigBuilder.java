package org.elisha.mybatis.framerwork.core.builder;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.io.Resources;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static org.elisha.mybatis.framerwork.core.utils.DocumentUtils.createDocument;

/**
 * @Description: 解析全局配置文件
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class XMLConfigBuilder {

    private Configuration configuration;

    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    public Configuration parseConfiguration(Element rootElement) {
        Element environments = rootElement.element("environments");
        parseEnvironment(environments);

        Element mappers = rootElement.element("mappers");
        parseMappers(mappers);
        return configuration;
    }

    /**
     *
     * @param environments
     */
    private void parseEnvironment(Element environments) {
        String aDefault = environments.attributeValue("default");
        List<Element> environmentList = environments.elements("environment");
        for (Element environment : environmentList) {
            String id = environment.attributeValue("id");
            if (aDefault.equals(id)) {
                parseDataSource(environment);
            }
        }
    }

    /**
     *
     * @param environment
     */
    private void parseDataSource(Element environment) {
        Element dataSource = environment.element("dataSource");
        String type = dataSource.attributeValue("type");
        if ("DBCP".equals(type)) {
            BasicDataSource ds = new BasicDataSource();
            Properties properties = parseProperties(dataSource);
            ds.setDriverClassName(properties.getProperty("db.driver"));
            ds.setUrl(properties.getProperty("db.url"));
            ds.setUsername(properties.getProperty("db.username"));
            ds.setPassword(properties.getProperty("db.password"));
            configuration.setDataSource(ds);
        }
    }

    private Properties parseProperties(Element dataSource) {
        List<Element> propertyList = dataSource.elements("property");
        Properties properties = new Properties();
        for (Element property : propertyList) {
            String name = property.attributeValue("name");
            String value = property.attributeValue("value");
            properties.put(name, value);
        }
        return properties;
    }

    /**
     *
     * @param mappers
     */
    private void parseMappers(Element mappers) {
        List<Element> mappersList = mappers.elements("mapper");
        for (Element mapper : mappersList) {
            String resource = mapper.attributeValue("resource");        // 获取配置文件对应的流对象
            InputStream inputStream = Resources.getResourceAsStream(resource);
            // 获取文件对应的 Document 对象
            Document document = createDocument(inputStream);
            // 按照指定的语义去解析 Document 文档对象
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(configuration);
            mapperBuilder.parseMapper(document.getRootElement());

        }
    }

}
