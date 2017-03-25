package com.arctro.ssn.backend.auth.password;

import com.arctro.ssn.backend.auth.PasswordBuilder;
import com.arctro.ssn.supporting.Utils;

/**
 * Generates a password by hashing the password and salt using SHA-256
 * @author Ben McLean
 */
public class SHA256PasswordBuilder implements PasswordBuilder{

	@Override
	public String hash(String password, String salt) {
		return Utils.sha256(password + salt);
	}

	@Override
	public String getName() {
		return "sha256";
	}

}
