package com.arctro.ssn.supporting.validators;

import com.arctro.inputvalidator.Validator;

public class UserTypeValidator implements Validator<Integer>{

	@Override
	public boolean isValid(Integer input) {
		return input != null && input < 3;
	}

	
	
}
