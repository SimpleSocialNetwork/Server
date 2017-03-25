package com.arctro.ssn.supporting.exceptions;

public class RateLimitException extends SSNException{
	private static final long serialVersionUID = 1L;

	public RateLimitException(){}

	public RateLimitException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RateLimitException(String message, Throwable cause) {
		super(message, cause);
	}

	public RateLimitException(String message) {
		super(message);
	}

	public RateLimitException(Throwable cause) {
		super(cause);
	}
	
	

}
