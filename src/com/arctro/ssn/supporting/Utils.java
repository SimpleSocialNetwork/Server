package com.arctro.ssn.supporting;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.arctro.ssn.protobuf.Protobuf;
import com.arctro.ssn.protobuf.models.impl.IPAddress;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

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
	
	public static String getIPAddress(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For"); 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		    ip = request.getHeader("Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		    ip = request.getHeader("WL-Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		    ip = request.getHeader("HTTP_CLIENT_IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		    ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		        ip = request.getRemoteAddr();  
		}
		
		return ip;
	}
	
	public static IPAddress getIPData(String address) throws UnirestException{
		HttpResponse<JsonNode> request = Unirest.get("http://ip-api.com/json/" + URLEncoder.encode(address))
				.asJson();
		
		JSONObject response = request.getBody().getObject();
		
		if(response.getString("status").equals("fail")){
			return null;
		}
		
		Protobuf.IPAddress.Builder ipB = Protobuf.IPAddress.newBuilder();
		ipB.setAddress(address);
		ipB.setLatitude(response.getDouble("latitude"));
		ipB.setLongitude(response.getDouble("longitude"));
		ipB.setCountry(response.getString("country"));
		ipB.setCountryCode(response.getString("countryCode"));
		ipB.setRegion(response.getString("region"));
		ipB.setRegionName(response.getString("regionName"));
		ipB.setCity(response.getString("city"));
		ipB.setIsp(response.getString("isp"));
		ipB.setTimezone(response.getString("timezone"));
	
		return new IPAddress(ipB.build());
	}
}
