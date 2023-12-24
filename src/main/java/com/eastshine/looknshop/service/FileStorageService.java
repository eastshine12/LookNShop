package com.eastshine.looknshop.service;

import com.eastshine.looknshop.exception.custom.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    public String storeFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new FileStorageException("File is empty or null.");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new FileStorageException("Invalid file name. Please provide a file with a valid extension.");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new FileStorageException("Unsupported file format. Allowed formats: " + String.join(", ", ALLOWED_EXTENSIONS));
        }

        String storeFileName = UUID.randomUUID().toString() + "." + extension;

        try {
            String fullPath = uploadDir + storeFileName;
            log.info("full Path : {}", fullPath);
            file.transferTo(new File(fullPath));

            return storeFileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + storeFileName, ex);
        }
    }
}
