package com.vtit.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	void createDirectory(String folder) throws URISyntaxException;
	String store(MultipartFile file, String folder) throws URISyntaxException, IOException;
	long getFileLength(String fileName, String folder) throws URISyntaxException;
	InputStreamResource getResource(String fileName, String folder) throws URISyntaxException, FileNotFoundException;
	void deleteFile(String folder, String fileName) throws URISyntaxException;
}
