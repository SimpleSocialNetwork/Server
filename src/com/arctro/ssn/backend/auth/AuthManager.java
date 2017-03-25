package com.arctro.ssn.backend.auth;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.arctro.ssn.protobuf.Protobuf;
import com.arctro.ssn.protobuf.models.impl.Session;
import com.arctro.ssn.protobuf.models.impl.SessionInformation;
import com.arctro.ssn.protobuf.models.impl.SessionSignature;
import com.arctro.ssn.protobuf.models.impl.ShortUser;
import com.arctro.ssn.supporting.SimpleRateLimiter;
import com.arctro.ssn.supporting.Utils;
import com.arctro.ssn.supporting.exceptions.IntegrityBrokenException;
import com.arctro.ssn.supporting.exceptions.InvalidSessionException;
import com.arctro.ssn.supporting.exceptions.InvalidUserException;
import com.arctro.ssn.supporting.exceptions.RateLimitException;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

public class AuthManager {
	
	public static SimpleRateLimiter<String> loginRateLimit = new SimpleRateLimiter<String>(5, TimeUnit.MINUTES, 5);
	
	Connection conn;
	
	public AuthManager(Connection conn){
		this.conn = conn;
	}
	
	public SessionInformation login(String email, String password) throws SQLException, InvalidUserException, RateLimitException{
		loginRateLimit.throwLimited(email);
		
		PreparedStatement prepared = conn.prepareStatement("SELECT `id`, `password`, `salt`, `password_version` FROM `users` WHERE `email` = ?");
		prepared.setString(1, email);
		ResultSet rs = prepared.executeQuery();
		
		if(!rs.next()){
			throw new InvalidUserException();
		}
		
		PasswordBuilder passwordBuilder = PasswordBuilderFactory.get(rs.getString("password_version"));
		String hashedPassword = passwordBuilder.hash(password, rs.getString("salt"));
		
		if(!hashedPassword.equals(rs.getString("password"))){
			throw new InvalidUserException();
		}
		
		if(!PasswordBuilderFactory.isNewest(rs.getString("password_version"))){
			//TODO change password
		}
		
		//TODO the stuff
		
		return null;
	}
	
	public SessionInformation register(String firstName, String lastName, String email, String password){
		return null;
	}
	
	public boolean isPasswordStrongEnough(int score, Protobuf.UserType userType){
		return (userType == Protobuf.UserType.USER && score >= 1) ||
				(userType == Protobuf.UserType.MOD && score >= 2) ||
				(userType == Protobuf.UserType.ADMIN && score == 4);
	}
	
	public int passwordStrength(String password){
		Zxcvbn zxcvbn = new Zxcvbn();
		Strength strength = zxcvbn.measure(password);
		return strength.getScore();
	}
	
	public ShortUser getSessionUser(byte[] serialized) throws InvalidProtocolBufferException, InvalidSessionException{
		Protobuf.SessionSignature si = Protobuf.SessionSignature.parseFrom(serialized);
		return getSessionUser(new SessionSignature(si));
	}
	
	public ShortUser getSessionUser(SessionSignature ss) throws InvalidSessionException{
		Protobuf.Session s = null;
		
		try{
			s = decrypt(ss).getBase();
		}catch(Exception e){
			throw new InvalidSessionException("The provided session was invalid", e);
		}
		
		return null;
	}
	
	public static SessionSignature encrypt(Session session) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		Key key = generateKey();
		Cipher c = Cipher.getInstance(AuthManagerKey.ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		
		Protobuf.Session s = session.getBase();
		Protobuf.SessionSignature.Builder ssb = Protobuf.SessionSignature.newBuilder();
		ssb.setSession(ByteString.copyFrom(c.doFinal(s.toByteArray())));
		ssb.setSignature(Utils.sha256(s.toByteArray()));
		
		return new SessionSignature(ssb.build());
	}
	
	public static Session decrypt(SessionSignature sessionSignature) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidProtocolBufferException, IllegalBlockSizeException, BadPaddingException, IntegrityBrokenException{
		Key key = generateKey();
		Cipher c = Cipher.getInstance(AuthManagerKey.ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		
		Protobuf.Session session = Protobuf.Session.parseFrom(c.doFinal(sessionSignature.getBase().getSession().toByteArray()));
		if(!verifyIntegrity(session, sessionSignature.getBase().getSignature())){
			throw new IntegrityBrokenException("The session has been modified or corrupted");
		}
		
		return new Session(session);
	}
	
	public static boolean verifyIntegrity(Protobuf.Session session, String signature){
		return Utils.sha256(session.toByteArray()).equals(signature);
	}
	
	private static Key generateKey(){
		return new SecretKeySpec(AuthManagerKey.key, AuthManagerKey.ALGO);
	}
}
