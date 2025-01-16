package com.learn.service.impl;

import com.learn.dto.FileDataDto;
import com.learn.service.FilePathService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FilePathServiceImpl implements FilePathService {

    @Value("${file_path}")
    private String path;

    private final Logger logger = LoggerFactory.getLogger(FilePathServiceImpl.class);

    @Override
    public String fileUpload(MultipartFile multipartFile) throws IOException {
        // Convert the given path string to a Path object
        Path filePath = Paths.get(path);

        // Check if the directory exists; if not, create it
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath);
            logger.info("Directory created: {}", filePath);
        }

        // Check if the file is null or empty
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new RuntimeException("File is null or empty. Please upload a valid file.");
        }

        // Get the uploaded file's name and extension
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new RuntimeException("Invalid file name.");
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (fileExtension.isEmpty()) {
            throw new RuntimeException("File does not have an extension.");
        }

        // Generate a new file name using the current timestamp
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "." + fileExtension;
        logger.info("File Name: {}", fileName);
        logger.info("File Extension: {}", fileExtension);
        logger.info("New File Name: {}", newFileName);

        // Create the full path for the new file
        Path newFilePath = filePath.resolve(newFileName);

        // Save the file in the specified directory with the new name
        Files.copy(multipartFile.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File uploaded successfully to: {}", newFilePath);

        return newFileName;
    }


}
