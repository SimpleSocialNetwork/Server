package com.arctro.ssn.backend.auth;

/**
 * Holds an algorithm to hash a password for storage in a database.<br/>
 * The hashed password is stored in the database next to its salt
 * and version (see {@link #getName()}), the versioning and
 * password builder exists so that if a more secure password hash
 * is required in the future it can be easily and automatically
 * upgraded. When a new password algorithm is implemented user's
 * hashes get automatically updated as they login.
 * @author Ben McLean
 */
public interface PasswordBuilder {
	/**
	 * Hashes a password
	 * @param password The password to hash
	 * @param salt The salt for the password
	 * @return A hashed password
	 */
	public String hash(String password, String salt);
	
	/**
	 * Returns a PasswordBuilder's name/version for use in 
	 * {@link PasswordFactory} and the database
	 * @return The PasswordBuilder's name/version
	 */
	public String getName();
}
