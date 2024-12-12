package com.grupo01.spring.controller.error;

public class DuplicateUserException extends RuntimeException {
	public DuplicateUserException(String message) {
		super(message);
	}
}
