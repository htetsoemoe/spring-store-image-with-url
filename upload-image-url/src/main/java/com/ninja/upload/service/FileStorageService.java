package com.ninja.upload.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ninja.upload.dto.MotorCycleDto;
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
	
	public List<MotorCycle> getAllProduct() {
		List<MotorCycle> cycles = repo.findAll();
		return cycles;
	}
	
	public Resource load(String fileName) {
		try {
			
			Path file = root.resolve(fileName);
			Resource resource = new UrlResource(file.toUri());
			
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
			
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	// Update product services
	public Optional<MotorCycle> getSingleProduct(int id) {
		return repo.findById(id);
	}
	
	public MotorCycle saveProduct(MotorCycle cycle) {
		return repo.save(cycle);
	}
	
	public String updateImage(MultipartFile image) throws IOException {
		
		Path filePath = root.resolve(image.getOriginalFilename());
		
		// saved image file to upload folder
		Files.copy(image.getInputStream(), filePath);
		
		return filePath.toString();
	}
	
	// end of product services
	
	// delete api
	
	public void deleteByProductId(int id) {
		if (repo.existsById(id)) {
			repo.deleteById(id);
		}
	}
	
	// end of delete api

}
