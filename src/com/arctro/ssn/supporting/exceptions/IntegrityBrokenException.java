package com.arctro.ssn.supporting.exceptions;

public class IntegrityBrokenException extends SSNException{
	private static final long serialVersionUID = 1L;
	
	public IntegrityBrokenException(){}

	public IntegrityBrokenException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public IntegrityBrokenException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IntegrityBrokenException(String arg0) {
		super(arg0);
	}

	public IntegrityBrokenException(Throwable arg0) {
		super(arg0);
	}
	
	

}
