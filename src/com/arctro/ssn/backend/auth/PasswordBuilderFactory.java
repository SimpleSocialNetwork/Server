package com.arctro.ssn.backend.auth;

import com.arctro.ssn.backend.auth.password.SHA256PasswordBuilder;

public class PasswordBuilderFactory {
	
	public static final String newestName = new SHA256PasswordBuilder().getName();
	
	public static PasswordBuilder get(){
		return get(null);
	}
	
	public static PasswordBuilder get(String name){
		switch(name){
		default:
			return new SHA256PasswordBuilder();
		}
	}
	
	public static boolean isNewest(String name){
		return name.equals(newestName);
	}

}
