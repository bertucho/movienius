package com.movienius.controllers;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String name) {
		super("Could not find user '" + name + "'.");
	}
}

@ResponseStatus(HttpStatus.CONFLICT)
class UserConflictException extends RuntimeException {

	public UserConflictException(String name) {
		super("Username '" + name + "' already exists.");
	}
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class MovieNotFoundException extends RuntimeException {

	public MovieNotFoundException() {
		super("Could not find movie");
	}
}

@ResponseStatus(HttpStatus.CONFLICT)
class MovieConflictException extends RuntimeException {

	public MovieConflictException(String title) {
		super("Movie '" + title + "' already exists.");
	}
}