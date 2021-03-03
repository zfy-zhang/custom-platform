package org.elisha.mybatis.framerwork.core.builder;

import org.elisha.mybatis.framerwork.core.config.Configuration;
import org.elisha.mybatis.framerwork.core.factory.DefaultSqlSessionFactory;
import org.elisha.mybatis.framerwork.core.factory.SqlSessionFactory;
import org.dom4j.Document;

import java.io.InputStream;
import java.io.Reader;

import static org.elisha.mybatis.framerwork.core.utils.DocumentUtils.createDocument;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/29
 * @Modify
 * @since
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream inputStream) {
        // 通过字节流封装 Configuration
        XMLConfigBuilder configBuilder = new XMLConfigBuilder();
        // 获取文件对应的 Document 对象
        Document document = createDocument(inputStream);
        Configuration configuration = configBuilder.parseConfiguration(document.getRootElement());;
        return build(configuration);
    }

    public SqlSessionFactory build(Reader reader) {
        // 通过字符流封装 Configuration
        Configuration configuration = null;
        return build(configuration);
    }

    private SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }
}
