package com.arctro.ssn.supporting.validators;

import com.arctro.inputvalidator.impl.NumberValidator;
import com.arctro.inputvalidator.impl.StringValidator;

public class ValidatorFacade {
	public static boolean isNumberValid(Number n, double max, double min){
		NumberValidator<Number> nv = new NumberValidator<Number>(max, min);
		return nv.isValid(n);
	}
	
	public static boolean isNumberPositive(Number n){
		NumberValidator<Number> nv = new NumberValidator<Number>(Double.MAX_VALUE, 0);
		return nv.isValid(n);
	}
	
	public static boolean isStringValid(String s, String regex){
		StringValidator sv = new StringValidator(regex);
		return sv.isValid(s);
	}
}
