package com.learn.service;

import com.learn.dto.FileDataDto;
import com.learn.dto.ResourceDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FilePathService {


    String fileUpload(MultipartFile file) throws IOException;

    ResourceDto getFileResourceById(String Id) throws MalformedURLException;


}
