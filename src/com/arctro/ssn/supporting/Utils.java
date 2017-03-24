package com.arctro.ssn.supporting;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Utils {
	
	public static String sha256(String s){
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
			return new String(hash, StandardCharsets.UTF_8);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static String sha256(byte[] s){
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(s);
			return new String(hash, StandardCharsets.UTF_8);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
