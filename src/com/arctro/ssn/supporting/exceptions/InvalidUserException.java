package com.arctro.ssn.supporting.exceptions;

public class InvalidUserException extends SSNException{
	private static final long serialVersionUID = 1L;

	public InvalidUserException(){}

	public InvalidUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUserException(String message) {
		super(message);
	}

	public InvalidUserException(Throwable cause) {
		super(cause);
	}
	
	

}
