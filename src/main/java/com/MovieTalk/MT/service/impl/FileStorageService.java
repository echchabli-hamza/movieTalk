package com.MovieTalk.MT.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads/}")
    private String uploadDir;
    
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        "jpg", "jpeg", "png" , "webp"
    );
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; 

    public String save(MultipartFile file) {
        
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 10MB");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("File must have a name");
        }
        
        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new IllegalArgumentException("File type not supported. Allowed: " + ALLOWED_EXTENSIONS);
        }

        try {
            String fileName = UUID.randomUUID() + "_" + originalFilename;
            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(fileName);

            // Create directories if they don't exist
            Files.createDirectories(uploadPath);
            
            // Write file
            Files.write(filePath, file.getBytes());

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }
    
    public boolean delete(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage(), e);
        }
    }
    
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}