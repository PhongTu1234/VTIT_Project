package com.vtit.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vtit.dto.response.file.ResUploadFileDTO;
import com.vtit.exception.StorageException;
import com.vtit.service.FileService;
import com.vtit.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class FileController {

	@Value("${upload-file.base-uri}")
	private String baseUri;

	private final FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	@PostMapping("/file")
	@ApiMessage("Upload a file")
	public ResponseEntity<ResUploadFileDTO> upload(@RequestParam(name = "file", required = false) MultipartFile file,
			@RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
		// skip validate
		if (file.isEmpty() || file == null) {
			throw new StorageException("File is Empty. Please upload a file.");
		}
		String fileName = file.getOriginalFilename();
		List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "png", "docx", "doc");
		boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
		if (!isValid) {
			throw new StorageException("Invalid file extension. only allows " + allowedExtensions.toString());
		}

		// create a directory if not exist
		fileService.createDirectory(baseUri + folder);

		// store file
		String uploadFileName = fileService.store(file,"avatars", folder);

		ResUploadFileDTO res = new ResUploadFileDTO();
		res.setFileName(uploadFileName);
		res.setUploadedAt(Instant.now());
		return ResponseEntity.ok(res);
	}
	
	@GetMapping("/file")
	@ApiMessage("Download a file")
	public ResponseEntity<Resource> download(@RequestParam(name = "fileName", required = false) String fileName,
											@RequestParam(name = "folder", required = false) String folder)
			throws StorageException, URISyntaxException, FileNotFoundException {
		if (fileName == null || folder == null) {
			throw new StorageException("Missing required params : (fileName or folder)");
		}
		
		Long fileLength = fileService.getFileLength(fileName, folder);
		if (fileLength == 0) {
			throw new StorageException("File with name = " + fileName + "not found.");
		}
		
		//download file
		InputStreamResource resource = fileService.getResource(fileName, folder);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.contentLength(fileLength)
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}
}
