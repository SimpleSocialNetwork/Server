package com.arctro.ssn.backend.auth;

import com.arctro.inputvalidator.Validator;
import com.arctro.ssn.protobuf.Protobuf;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

/**
 * Validates that a password is strong enough
 * @author Ben McLean
 *
 */
public class PasswordValidator implements Validator<String> {

	Protobuf.UserType userType;
	
	public PasswordValidator(Protobuf.UserType userType) {
		this.userType = userType;
	}
	
	public PasswordValidator(){
		userType = Protobuf.UserType.USER;
	}

	/**
	 * Checks the inputted password's strength
	 * @param password The password to check
	 * @return The inputted password's strength
	 */
	@Override
	public boolean isValid(String input) {
		int score = getScore((String) input);
		
		return (userType == Protobuf.UserType.USER && score >= 1) ||
				(userType == Protobuf.UserType.MOD && score >= 2) ||
				(userType == Protobuf.UserType.ADMIN && score == 4);
	}
	
	/**
	 * Checks the inputted password's strength
	 * @param password The password to check
	 * @return The inputted password's strength
	 */
	public int getScore(String password){
		Zxcvbn zxcvbn = new Zxcvbn();
		Strength strength = zxcvbn.measure(password);
		return strength.getScore();
	}

}
