package com.learn.service;

import com.learn.dto.FileDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileDataService {

    FileDataDto create(FileDataDto fileDataDto);

    FileDataDto getById(String Id);

    List<FileDataDto> getAll();

    FileDataDto setBanner(MultipartFile multipartFile, String Id) throws IOException;


}
