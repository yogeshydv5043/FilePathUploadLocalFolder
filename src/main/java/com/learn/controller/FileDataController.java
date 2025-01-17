package com.learn.controller;

import com.learn.dto.FileDataDto;
import com.learn.dto.ResourceDto;
import com.learn.model.FileData;
import com.learn.service.FileDataService;
import com.learn.service.FilePathService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/file/v1")
public class FileDataController {

    private final Logger logger = LoggerFactory.getLogger(FileDataController.class);


    @Autowired
    private FilePathService filePathService;

    @Autowired
    private FileDataService fileDataService;

    @PostMapping("/")
    public ResponseEntity<FileDataDto> create(@RequestBody FileDataDto fileDataDto) throws IOException {
        FileDataDto response = fileDataService.create(fileDataDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/banner/{Id}")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("Id") String Id) throws IOException {
        FileDataDto response = fileDataService.setBanner(file, Id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<FileDataDto>> getAll() {
        return ResponseEntity.ok(fileDataService.getAll());
    }


    @GetMapping("/resource/{Id}")
    public ResponseEntity<Resource> getFileResourceById(@PathVariable String Id) throws MalformedURLException {
        // Call service method to get the resource and MIME type
        ResourceDto resourceDto = filePathService.getFileResourceById(Id);

        // Return the resource as response with the correct MIME type
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resourceDto.getType())) // Set MIME type dynamically
                .body(resourceDto.getResource()); // Return the file as response body
    }


}
