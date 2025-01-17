package com.learn.service.impl;

import com.learn.dto.FileDataDto;
import com.learn.dto.ResourceDto;
import com.learn.model.FileData;
import com.learn.repository.FileDataRepository;
import com.learn.service.FileDataService;
import com.learn.service.FilePathService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FilePathServiceImpl implements FilePathService {

    @Value("${multipart_file_path}")
    private String multipart_file_path;

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private ModelMapper modelMapper;


    private final Logger logger = LoggerFactory.getLogger(FilePathServiceImpl.class);

    @Override
    public String fileUpload(MultipartFile multipartFile) throws IOException {
        // Path where the file will be stored
        Path filePath = Paths.get(multipart_file_path);

        // If the directory doesn't exist, create it
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath);
            logger.info("Directory created: {}", filePath);
        }

        // Check if the uploaded file is empty or null
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new RuntimeException("Please upload a valid file.");
        }

        // Get the file type (JPEG, PNG, etc.)
        String contentType = multipartFile.getContentType();
        // Only allow images with JPEG, JPG, or PNG type
        if (contentType == null ||
                (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new RuntimeException("Invalid file type! Only JPEG and PNG images are allowed.");
        }

        // Get the file name
        String fileName = multipartFile.getOriginalFilename();
        // Make sure file name is valid
        if (fileName == null || fileName.isEmpty()) {
            throw new RuntimeException("File name is invalid.");
        }

        // Extract the file extension using the helper method
        String fileExtension = getFileExtension(fileName);

        // Only allow extensions PNG, JPG, or JPEG
        if (!fileExtension.equals("png") && !fileExtension.equals("jpg") && !fileExtension.equals("jpeg")) {
            throw new RuntimeException("Invalid file extension! Only PNG, JPG, or JPEG files are allowed.");
        }

        // Create a new name for the file using the current timestamp
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "." + fileExtension;
        logger.info("New File Name: {}", newFileName);

        // Save the file with the new name
        Path newFilePath = filePath.resolve(newFileName);
        Files.copy(multipartFile.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File uploaded successfully to: {}", newFilePath);

        // Return the new file name
        return newFileName;
    }

    @Override
    public ResourceDto getFileResourceById(String Id) throws MalformedURLException {
        // Fetch file details using the provided ID
        FileData fileData = fileDataRepository.findById(Id).orElseThrow(() -> new RuntimeException("File Data not found : {} !! "));
        // Converting FileData to FileDataDto
        FileDataDto fileDataDto = modelMapper.map(fileData, FileDataDto.class);
        // Build the full file path
        String fullFilePath = multipart_file_path + "/" + fileDataDto.getBanner();
        // Create Path object for the file
        Path path = Paths.get(fullFilePath);
        // Create Resource object from file's URI
        Resource resource = new UrlResource(path.toUri());

        // Check if resource exists
        if (resource.exists()) {
            try {
                // Get MIME type for the file
                String mimeType = Files.probeContentType(path);
                // If MIME type is null (couldn't detect), set to default
                if (mimeType == null) {
                    mimeType = "application/octet-stream"; // Default for unknown types
                }
                // Return ResourceDto containing the resource and MIME type
                return new ResourceDto(mimeType, resource);
            } catch (IOException e) {
                throw new RuntimeException("Failed to determine file type: " + e.getMessage());
            }
        } else {
            // If the file does not exist, throw an error
            throw new RuntimeException("File not found: " + fullFilePath);
        }
    }

    // Helper method to extract file extension
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new RuntimeException("Invalid file name. No extension found.");
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }


}
