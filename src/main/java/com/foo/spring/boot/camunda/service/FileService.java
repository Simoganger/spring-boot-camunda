package com.foo.spring.boot.camunda.service;

import com.foo.spring.boot.camunda.exception.AppCommonException;
import com.foo.spring.boot.camunda.exception.AppDBItemNotFoundException;
import com.foo.spring.boot.camunda.model.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.MalformedURLException;

public interface FileService {
    
    File saveFile(File file);
    File createFile(MultipartFile file, String validExtension, String fileUniqueName);
    File getFile(String id) throws AppDBItemNotFoundException;
    void deleteFile(String id) throws AppDBItemNotFoundException;
    String storeBpmn(MultipartFile file, String uuid) throws IOException;
    String store(MultipartFile file, String uuid) throws IOException;
    Resource read(String uuid) throws MalformedURLException;
    boolean delete(String uuid);
    String getProcessId(String filePath) throws ParserConfigurationException, AppCommonException, SAXException, XPathExpressionException, IOException;
}

