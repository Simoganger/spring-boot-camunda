package com.foo.spring.boot.camunda.service.impl;

import com.foo.spring.boot.camunda.dto.CustomProcessDto;
import com.foo.spring.boot.camunda.exception.AppCommonException;
import com.foo.spring.boot.camunda.model.CustomProcess;
import com.foo.spring.boot.camunda.model.File;
import com.foo.spring.boot.camunda.repository.CustomProcessRepository;
import com.foo.spring.boot.camunda.service.CustomProcessService;
import com.foo.spring.boot.camunda.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.UUID;

@Service
public class CustomProcessServiceImpl implements CustomProcessService {

    @Autowired
    private CustomProcessRepository customProcessRepository;

    @Autowired
    private FileService fileService;

    @Override
    public CustomProcess saveCustomProcess(CustomProcess customProcess) {
        return customProcessRepository.save(customProcess);
    }

    @Override
    public CustomProcess createProcess(MultipartFile file, CustomProcessDto customProcessDto) throws IOException, ParserConfigurationException, AppCommonException, SAXException, XPathExpressionException {

        String uuid = UUID.randomUUID().toString();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String validExtension;
        if(extension != null){
            validExtension = extension.toLowerCase();
        }else{
            validExtension = "bpmn";
        }
        String fileUniqueName = uuid + "." + validExtension;
        String filePath = fileService.storeBpmn(file, fileUniqueName);
        String processId = fileService.getProcessId(filePath);

        File savedBpmFile = fileService.createFile(file, validExtension, fileUniqueName);

        CustomProcess process = new CustomProcess();
        process.setName(customProcessDto.getName());
        process.setBpmnFile(savedBpmFile);
        process.setProcessId(processId);
        return saveCustomProcess(process);
    }
}
