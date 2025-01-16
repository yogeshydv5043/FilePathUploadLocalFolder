package com.learn.service;

import com.learn.dto.FileDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FilePathService {


    String fileUpload(MultipartFile file) throws IOException;


}
