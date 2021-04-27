package com.foo.spring.boot.camunda.service.impl;

import com.foo.spring.boot.camunda.dto.CustomProcessDto;
import com.foo.spring.boot.camunda.exception.AppCommonException;
import com.foo.spring.boot.camunda.exception.AppDBItemNotFoundException;
import com.foo.spring.boot.camunda.exception.EnumErrorCode;
import com.foo.spring.boot.camunda.model.CustomProcess;
import com.foo.spring.boot.camunda.model.File;
import com.foo.spring.boot.camunda.repository.CustomProcessRepository;
import com.foo.spring.boot.camunda.service.CustomProcessService;
import com.foo.spring.boot.camunda.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@Service
public class CustomProcessServiceImpl implements CustomProcessService {

    @Autowired
    private CustomProcessRepository customProcessRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public CustomProcess findById(String id) throws AppDBItemNotFoundException {
        return customProcessRepository.findById(id)
                .orElseThrow(()-> new AppDBItemNotFoundException(EnumErrorCode.ERROR_DB_ITEM_NOTFOUND, CustomProcess.class.getSimpleName(), "id", id));
    }

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

    @Override
    public String deployProcess(String id) throws AppDBItemNotFoundException, IOException {
        CustomProcess customProcess = findById(id);
        return createDeployment(customProcess);
    }

    @Override
    public String createDeployment(CustomProcess customProcess) throws IOException {
        Resource resource = fileService.read(customProcess.getBpmnFile().getUuid());
        Deployment deployment = repositoryService.createDeployment()
                .addInputStream(customProcess.getBpmnFile().getName(), resource.getInputStream())
                .deploy();
        return deployment.getId();
    }

    @Override
    public String startInstance(String id) throws AppDBItemNotFoundException {
        CustomProcess customProcess = findById(id);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(customProcess.getProcessId());
        return runtimeService.startProcessInstanceByKey(customProcess.getProcessId()).getProcessInstanceId();
    }
}
