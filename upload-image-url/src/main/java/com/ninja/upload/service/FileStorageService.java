package com.ninja.upload.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ninja.upload.entity.MotorCycle;
import com.ninja.upload.repo.MotorCycleRepo;

@Service
public class FileStorageService {
	
	@Autowired
	private MotorCycleRepo repo;
	
	private Path root = Paths.get("uploads");
	
	public String uploadProductToFileSystem(MultipartFile image, String name, String description, double price) throws IOException {
		Path filePath = root.resolve(image.getOriginalFilename());
		
		MotorCycle cycle = new MotorCycle();
		cycle.setImage(filePath.toString());
		cycle.setName(name);
		cycle.setDescription(description);
		cycle.setPrice(price);
		
		// saved motor cycle entity to database
		MotorCycle savedCycle =  repo.save(cycle);
		
		// saved image file to upload folder
		Files.copy(image.getInputStream(), filePath);
		
		if (null != savedCycle) {
			return "File uploaded successfully at : %s".formatted(filePath);
		}
		
		return null;
	}

}
