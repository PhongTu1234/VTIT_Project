package com.vtit.service.impl;

import com.vtit.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = Logger.getLogger(FileServiceImpl.class.getName());

    @Value("${upload-file.base-uri}")
    private String baseUri;

    private Path resolveFolderPath(String folder) throws URISyntaxException {
        return Paths.get(new URI(baseUri)).resolve(folder).normalize();
    }

    private Path resolveFilePath(String folder, String fileName) throws URISyntaxException {
        return resolveFolderPath(folder).resolve(fileName).normalize();
    }

    @Override
    public void createDirectory(String folder) throws URISyntaxException {
        Path path = resolveFolderPath(folder);
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
                logger.info(">>> Directory created: " + path.toAbsolutePath());
            } catch (IOException e) {
                logger.severe(">>> Failed to create directory: " + e.getMessage());
                throw new RuntimeException("Failed to create directory: " + folder, e);
            }
        } else {
            logger.info(">>> Directory already exists: " + path.toAbsolutePath());
        }
    }

    @Override
    public String store(MultipartFile file, String folder, String fileName) throws URISyntaxException, IOException {
        Path destination = resolveFilePath(folder, fileName);
        createDirectory(folder); // ensure folder exists
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    @Override
    public long getFileLength(String fileName, String folder) throws URISyntaxException {
        Path path = resolveFilePath(folder, fileName);
        File file = path.toFile();
        return (file.exists() && file.isFile()) ? file.length() : 0;
    }

    @Override
    public InputStreamResource getResource(String fileName, String folder)
            throws URISyntaxException, FileNotFoundException {
        Path path = resolveFilePath(folder, fileName);
        File file = path.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + path);
        }
        return new InputStreamResource(new FileInputStream(file));
    }

    @Override
    public void deleteFile(String folder, String fileName) throws URISyntaxException {
        Path path = resolveFilePath(folder, fileName);
        try {
            Files.deleteIfExists(path);
            logger.info(">>> File deleted: " + path);
        } catch (IOException e) {
            logger.severe(">>> Failed to delete file: " + path);
            throw new RuntimeException("Cannot delete file: " + path, e);
        }
    }

    @Override
    public String generateFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        return UUID.randomUUID().toString() + extension;
    }
}
