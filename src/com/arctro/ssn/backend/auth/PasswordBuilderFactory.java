package com.arctro.ssn.backend.auth;

import com.arctro.ssn.backend.auth.password.SHA256PasswordBuilder;

/**
 * Gets the appropriate PasswordBuilder for the provided name
 * @author Ben McLean
 */
public class PasswordBuilderFactory {
	
	//The name of the PasswordBuilder currently in use
	public static final String newestName = new SHA256PasswordBuilder().getName();
	
	/**
	 * Gets the current PasswordBuilder
	 * @return The current PasswordBuilder
	 */
	public static PasswordBuilder get(){
		return get(null);
	}
	
	/**
	 * Gets the specified PasswordBuilder
	 * @param name The name of the PasswordBuilder to get
	 * @return The specified PasswordBuilder
	 */
	public static PasswordBuilder get(String name){
		switch(name){
		default:
			return new SHA256PasswordBuilder();
		}
	}
	
	/**
	 * Checks if the specified PasswordBuilder is current
	 * @param name The name of the PasswordBuilder to get
	 * @return If the specified PasswordBuilder is current
	 */
	public static boolean isNewest(String name){
		return name.equals(newestName);
	}

}
