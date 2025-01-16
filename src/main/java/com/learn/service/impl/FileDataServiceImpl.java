package com.learn.service.impl;

import com.learn.dto.FileDataDto;
import com.learn.model.FileData;
import com.learn.repository.FileDataRepository;
import com.learn.service.FileDataService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class FileDataServiceImpl implements FileDataService {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private FilePathServiceImpl filePathService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FileDataDto create(FileDataDto fileDataDto) {
        fileDataDto.setId(UUID.randomUUID().toString());
        FileData fileData = modelMapper.map(fileDataDto, FileData.class);
        FileData response = fileDataRepository.save(fileData);
        return modelMapper.map(response, FileDataDto.class);
    }

    @Override
    public List<FileDataDto> getAll() {
        List<FileData> list = fileDataRepository.findAll();
        return list.stream().map(file -> modelMapper.map(file, FileDataDto.class)).toList();
    }

    @Override
    public FileDataDto setBanner(MultipartFile multipartFile, String Id) throws IOException {
        FileData response = fileDataRepository.findById(Id).orElseThrow(() -> new RuntimeException("Id not found !!"));
        String banner=filePathService.fileUpload(multipartFile);
        response.setBanner(banner);
        FileData fileData = fileDataRepository.save(response);
        return modelMapper.map(fileData, FileDataDto.class);
    }
}
