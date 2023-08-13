package com.ninja.upload.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ninja.upload.service.FileStorageService;

@RestController
@RequestMapping("/products")
public class FileUploadController {
	
	@Autowired
	private FileStorageService storageService;
	
	@PostMapping
	public ResponseEntity<?> uploadProductToFileSystem(@RequestParam("image") MultipartFile image, @RequestParam("name") String name, 
			@RequestParam("description") String description, @RequestParam("price") double price) throws IOException {
		
		String uploadedProduct = storageService.uploadProductToFileSystem(image, name, description, price);
		
		return ResponseEntity.status(HttpStatus.OK).body(uploadedProduct);
	}

}
