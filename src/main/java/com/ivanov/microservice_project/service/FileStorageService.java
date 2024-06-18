package com.ivanov.microservice_project.service;

import com.ivanov.microservice_project.exception.FileNotFoundExtException;
import com.ivanov.microservice_project.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void storeFile(MultipartFile file, Long projectId) {
        Path projectDir = Paths.get(uploadDir + "/" + projectId);

        try {
            // Создание директории для проекта (если ее нет)
            Files.createDirectories(projectDir);

            // Копирование содержимого загружаемого файла в директорию проекта
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, projectDir.resolve(file.getOriginalFilename()),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            // В случае ошибки выбрасываем исключение
            throw new FileStorageException("Could not store file " +
                    file.getOriginalFilename() + ". Please try again!", e);
        }
    }

    public Resource loadFileAsResource(Long projectId, String fileName) {
        try {
            Path filePath = Paths.get(uploadDir + "/" + projectId).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundExtException("File not found " + fileName, ex);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

