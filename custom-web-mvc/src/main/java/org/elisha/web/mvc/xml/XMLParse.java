package org.elisha.web.mvc.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class XMLParse {

    public static String getBasePackage(String xml) {
        try{
            SAXReader saxReader = new SAXReader();
            InputStream inputStream = XMLParse.class.getClassLoader().getResourceAsStream(xml);
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();
            Element componentScan = rootElement.element("component-scan");
            Attribute attribute = componentScan.attribute("base-package");
            String basePackage = attribute.getText();
            return basePackage;
        }catch (DocumentException e){
            e.printStackTrace();
        }finally {

        }
        return  "";
    }
}
