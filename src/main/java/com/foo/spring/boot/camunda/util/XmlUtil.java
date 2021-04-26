package com.foo.spring.boot.camunda.util;

import com.foo.spring.boot.camunda.exception.AppCommonException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.IOException;

public class XmlUtil {

    public static String getProcessIdFromXmlFileWithXpath(String filePath) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, AppCommonException {
        FileInputStream fileIS = new FileInputStream(FileUtil.read(filePath).getFile());
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(fileIS);
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "/definitions/process";
        Element element = (Element) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
        return element.getAttribute("id");
    }
}
