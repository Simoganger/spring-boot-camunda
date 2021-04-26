package com.foo.spring.boot.camunda.service;

import com.foo.spring.boot.camunda.dto.CustomProcessDto;
import com.foo.spring.boot.camunda.exception.AppCommonException;
import com.foo.spring.boot.camunda.exception.AppDBItemNotFoundException;
import com.foo.spring.boot.camunda.model.CustomProcess;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.MalformedURLException;

public interface CustomProcessService {
    CustomProcess findById(String id) throws AppDBItemNotFoundException;
    CustomProcess saveCustomProcess(CustomProcess customProcess);
    CustomProcess createProcess(MultipartFile file, CustomProcessDto customProcessDto) throws IOException, ParserConfigurationException, AppCommonException, SAXException, XPathExpressionException;
    String deployProcess(String id) throws AppDBItemNotFoundException, IOException;
    String createDeployment(CustomProcess customProcess) throws IOException;
    String startInstance(String deploymentId) throws AppDBItemNotFoundException;
}
