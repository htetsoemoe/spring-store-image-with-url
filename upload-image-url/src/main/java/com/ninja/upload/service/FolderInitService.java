package com.ninja.upload.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class FolderInitService {
	
	private final Path root = Paths.get("uploads");
	
	public void init() {
		try {
			Files.createDirectories(root);
		} catch (Exception e) {
			throw new RuntimeException("Could not initialize folder for upload.");
		}
	}

}
