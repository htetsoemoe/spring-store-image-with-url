package com.ninja.upload.exception;

public class ResourceNotFoundExceptio extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundExceptio(String message) {
		super(message);
	}
	
}
