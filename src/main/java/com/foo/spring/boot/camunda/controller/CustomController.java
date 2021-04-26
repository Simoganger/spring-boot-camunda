package com.foo.spring.boot.camunda.controller;

import com.foo.spring.boot.camunda.dto.CustomProcessDto;
import com.foo.spring.boot.camunda.exception.AppCommonException;
import com.foo.spring.boot.camunda.exception.AppDBItemNotFoundException;
import com.foo.spring.boot.camunda.service.CustomProcessService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class CustomController {

    @Autowired
    private CustomProcessService customProcessService;

    /**
     * Save a process in the process repository
     * @param file
     * @param customProcessDto
     * @return
     */
    @PostMapping("/process")
    @ApiOperation("Upload a process in the repository")
    public ResponseEntity<?> createProcess(@RequestPart("file") MultipartFile file,
                                       @RequestPart("process") CustomProcessDto customProcessDto) {
        try {
            return new ResponseEntity<>(customProcessService.createProcess(file, customProcessDto), HttpStatus.CREATED);
        } catch (IOException | ParserConfigurationException | AppCommonException | SAXException | XPathExpressionException e) {
            return new ResponseEntity<>("An exception occured while loading the process " + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * Deploy a process
     * @param id
     * @return The id of the deployment
     * @throws IOException
     * @throws AppDBItemNotFoundException
     */
    @PostMapping("/process/deploy/{id}")
    @ApiOperation("Deploy a process")
    public ResponseEntity<?> deployProcess(@PathVariable String id) throws IOException, AppDBItemNotFoundException {
        String deploymentId = customProcessService.deployProcess(id);
        return new ResponseEntity<>("Process deployed successfully with deployment id " + deploymentId, HttpStatus.OK);
    }

    /**
     * Start a process
     * @param id
     * @return The id of the started instance
     * @throws AppDBItemNotFoundException
     */
    @PostMapping("/process/start/{id}")
    @ApiOperation("Start an instance of process")
    public ResponseEntity<?> startProcess(@PathVariable String id) throws AppDBItemNotFoundException {
        String processInstanceId = customProcessService.startInstance(id);
        return new ResponseEntity<>("Instance started successfully with id " + processInstanceId, HttpStatus.OK);
    }
}
