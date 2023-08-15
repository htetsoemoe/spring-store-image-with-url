package com.ninja.upload.dto;

import com.ninja.upload.entity.MotorCycle;

public class MotorCycleDto {
	
	private int id;
	private String name;
	private double price;
	private String image;
	private String description;
	
	public MotorCycleDto() {
	
	}

	public MotorCycleDto(int id, String name, double price, String url, String description) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = url;
		this.description = description;
	}
	
	public MotorCycleDto(MotorCycle cycle) {
		this.name = cycle.getName();
		this.description = cycle.getDescription();
		this.price = cycle.getPrice();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	

}
