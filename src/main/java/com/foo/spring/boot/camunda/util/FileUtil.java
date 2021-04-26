package com.foo.spring.boot.camunda.util;

import com.foo.spring.boot.camunda.exception.AppCommonException;
import com.foo.spring.boot.camunda.exception.EnumErrorCode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.nio.file.Path;

public class FileUtil {
    public static Resource read(String filePath) throws MalformedURLException, AppCommonException {
        Path file = new java.io.File(filePath).toPath();
        Resource resource = new UrlResource(file.toUri());
        if(resource.exists() || resource.isReadable()){
            return resource;
        }else{
            throw new AppCommonException(EnumErrorCode.ERROR_DB_ITEM_NOTFOUND, "Unble to load ");
        }
    }
}
