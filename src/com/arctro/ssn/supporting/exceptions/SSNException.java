package com.arctro.ssn.supporting.exceptions;

public class SSNException extends Exception{
	private static final long serialVersionUID = 1L;

	public SSNException() {
		super();
	}

	public SSNException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public SSNException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SSNException(String arg0) {
		super(arg0);
	}

	public SSNException(Throwable arg0) {
		super(arg0);
	}
	
	

}
