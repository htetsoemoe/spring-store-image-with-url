package com.ninja.upload.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.ninja.upload.dto.MotorCycleDto;
import com.ninja.upload.entity.MotorCycle;
import com.ninja.upload.service.FileStorageService;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class FileUploadController {
	
	@Autowired
	private FileStorageService storageService;
	
	// Create product
	@PostMapping
	public ResponseEntity<?> uploadProductToFileSystem(@RequestParam("image") MultipartFile image, @RequestParam("name") String name, 
			@RequestParam("description") String description, @RequestParam("price") double price) throws IOException {
		
		String uploadedProduct = storageService.uploadProductToFileSystem(image, name, description, price);
		
		return ResponseEntity.status(HttpStatus.OK).body(uploadedProduct);
	}
	
	// Get specific product with id
	@GetMapping("/{id}")
	public ResponseEntity<MotorCycleDto> getProductById(@PathVariable int id) throws FileNotFoundException {
		
		MotorCycleDto foundCycle =  storageService.getProductWithId(id).map(cycle -> {		
			String name = cycle.getName();
			int cycleId = cycle.getId();
			String description = cycle.getDescription();
			double price = cycle.getPrice();
			
			String image = cycle.getImage();
			String fileName = image.substring(8);
			
			String url = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "getFile", fileName).build().toString();
			
			return new MotorCycleDto(cycleId, name, price, url, description);
			
		}).orElseThrow(() -> new FileNotFoundException("Not found product with ID %".formatted(id)));
		
		return ResponseEntity.status(HttpStatus.OK).body(foundCycle);	
	}
	
	// Get all product
	@GetMapping
	public ResponseEntity<List<MotorCycleDto>> getAllProduct() {
		List<MotorCycleDto> cycles = storageService.getAllProduct().stream().map(cycle -> {
			
			String name = cycle.getName();
			int id = cycle.getId();
			String description = cycle.getDescription();
			double price = cycle.getPrice();
			
			// need to change url
			String image = cycle.getImage(); // 'uploads/product1.jpg'
			String fileName = image.substring(8); // 'product1.jpg'
			
			String url = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "getFile", fileName).build().toString();
			
			return new MotorCycleDto(id, name, price, url, description);
		}).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(cycles);
	}
	
	/**
	 * 
	 * We have @Path expression to /files/{filename : .+}. 
	 * The .+ is a regular expression that will match any stream of characters after /files.
	 * So, the GET /files/work/citys.txt request would be routed to getFile().
	 */
	@GetMapping("/images/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.load(filename);	
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	// Delete product with specific id
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteProductById(@PathVariable int id) {
		storageService.deleteByProductId(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// Update product with form
	@PostMapping("/{id}")
	public ResponseEntity<MotorCycleDto> updateProduct(@PathVariable int id, @RequestParam("image") Optional<MultipartFile> image, @RequestParam("name") String name, 
			@RequestParam("description") String description, @RequestParam("price") double price) throws IOException {
		
		MotorCycle cycle = storageService.getSingleProduct(id)
				.orElseThrow(() -> new RuntimeException("Not found product with ID %".formatted(id)));
		
		cycle.setName(name);
		cycle.setDescription(description);
		cycle.setPrice(price);
		
		if (image.isPresent()) {
			cycle.setImage(storageService.updateImage(image.get()));
		}
		
		
		MotorCycle savedCycle = storageService.saveProduct(cycle);
		
		return new ResponseEntity<MotorCycleDto>(new MotorCycleDto(savedCycle) , HttpStatus.CREATED);
	}
	
	/* update product with request body => JSON 
	@PutMapping("/{id}")
	public ResponseEntity<MotorCycleDto> updateProduct(@PathVariable int id, @RequestBody MotorCycleDto dto) {
		
		MotorCycle cycle = storageService.updateSingleProduct(id)
				.orElseThrow(() -> new RuntimeException("Not found product with ID %".formatted(id)));
		
		cycle.setName(dto.getName());
		cycle.setDescription(dto.getDescription());
		cycle.setPrice(dto.getPrice());
		
		MotorCycle savedCycle = storageService.saveProduct(cycle);
		
		return new ResponseEntity<MotorCycleDto>(new MotorCycleDto(savedCycle) , HttpStatus.CREATED);
	} 
	*/

}
