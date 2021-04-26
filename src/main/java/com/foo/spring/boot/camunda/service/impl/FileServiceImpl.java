package com.foo.spring.boot.camunda.service.impl;

import com.foo.spring.boot.camunda.exception.AppCommonException;
import com.foo.spring.boot.camunda.exception.AppDBItemNotFoundException;
import com.foo.spring.boot.camunda.exception.EnumErrorCode;
import com.foo.spring.boot.camunda.model.File;
import com.foo.spring.boot.camunda.repository.FileRepository;
import com.foo.spring.boot.camunda.service.FileService;
import com.foo.spring.boot.camunda.util.XmlUtil;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LogManager.getLogger(FileServiceImpl.class);

    @Value("${app.storage.directory}")
    private String storageDirectory;

    @Autowired
    private FileRepository fileRepository;

    @Override
    public File saveFile(File file) {
        return fileRepository.save(file);
    }

    @Override
    public File createFile(MultipartFile file, String validExtension, String fileUniqueName) {
        File bpmnFile = new File();
        bpmnFile.setSize(file.getSize());
        bpmnFile.setUuid(fileUniqueName);
        bpmnFile.setName(file.getOriginalFilename());
        bpmnFile.setMimeType(file.getContentType());
        bpmnFile.setExtension(validExtension);
        return fileRepository.save(bpmnFile);
    }

    @Override
    public File getFile(String id) throws AppDBItemNotFoundException {
        return fileRepository.findById(id)
                .orElseThrow(()-> new AppDBItemNotFoundException(EnumErrorCode.ERROR_DB_ITEM_NOTFOUND, File.class.getSimpleName(), "id", id));
    }

    @Override
    public void deleteFile(String id) throws AppDBItemNotFoundException {
        File fileToDelete = getFile(id);
        fileRepository.delete(fileToDelete);
    }

    @Override
    public String storeBpmn(MultipartFile file, String uuid) throws IOException {

        java.io.File storageUrl = new java.io.File(storageDirectory);
        storageUrl.mkdirs();

        Path storagePath = storageUrl.toPath();
        Files.copy(file.getInputStream(), storagePath.resolve(uuid));

        return storagePath + java.io.File.separator + uuid;
    }

    @Override
    public String store(MultipartFile file, String uuid) throws IOException {

        java.io.File storageUrl = new java.io.File(storageDirectory);
        storageUrl.mkdirs();

        Path storagePath = storageUrl.toPath();

        Files.copy(file.getInputStream(), storagePath.resolve(uuid));

        return uuid;
    }

    @Override
    public Resource read(String uuid) throws MalformedURLException {
        Path readPath = new java.io.File(storageDirectory).toPath();
        Path file = readPath.resolve(uuid);
        Resource resource = new UrlResource(file.toUri());
        if(resource.exists() || resource.isReadable()){
            return resource;
        }else{
            throw new MalformedURLException("Unable to load process file.");
        }
    }

    @Override
    public boolean delete(String uuid) {
        java.io.File fileToDelete = FileUtils.getFile(storageDirectory + java.io.File.separator + uuid);
        return FileUtils.deleteQuietly(fileToDelete);
    }

    @Override
    public String getProcessId(String filePath) throws ParserConfigurationException, AppCommonException, SAXException, XPathExpressionException, IOException {
        return XmlUtil.getProcessIdFromXmlFileWithXpath(filePath);
    }
}
