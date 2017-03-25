package com.arctro.ssn.backend.auth;

public interface PasswordBuilder {
	public String hash(String password, String salt);
	public String getName();
}
