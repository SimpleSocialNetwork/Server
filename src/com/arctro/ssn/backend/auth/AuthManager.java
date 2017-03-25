package com.arctro.ssn.backend.auth;

import java.nio.charset.StandardCharsets;
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
import com.arctro.ssn.supporting.SSNContext;
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

/**
 * Manages user authentication
 * @author Ben McLean
 */
public class AuthManager {
	
	//Rate limits a username's login attempts
	public static SimpleRateLimiter<String> loginRateLimit = new SimpleRateLimiter<String>(5, TimeUnit.MINUTES, 5);
	
	Connection conn;
	SSNContext context;
	
	/**
	 * Creates a new AuthManager instance
	 * @param context The request context
	 */
	public AuthManager(SSNContext context){
		this.context = context;
		this.conn = context.getConnection();
	}
	
	/**
	 * Authenticates a user
	 * @param email The email address of the user
	 * @param password The password of the user
	 * @return An object containing user information and a session signature
	 * @throws SQLException Thrown when an SQLException occurs
	 * @throws InvalidUserException The user does not exist
	 * @throws RateLimitException The user's login has been rate limited
	 */
	public SessionInformation login(String email, String password) throws SQLException, InvalidUserException, RateLimitException{
		//Throw an exception if rate limited
		loginRateLimit.throwLimited(email);
		
		//Fetch user details from database
		PreparedStatement prepared = conn.prepareStatement("SELECT `id`, `password`, `salt`, `password_version` FROM `users` WHERE `email` = ?");
		prepared.setString(1, email);
		ResultSet rs = prepared.executeQuery();
		
		//If user does not exist
		if(!rs.next()){
			throw new InvalidUserException();
		}
		
		//Get the PasswordBuilder for the user
		PasswordBuilder passwordBuilder = PasswordBuilderFactory.get(rs.getString("password_version"));
		String hashedPassword = passwordBuilder.hash(password, rs.getString("salt"));
		
		//Check if the password is correct
		if(!hashedPassword.equals(rs.getString("password"))){
			throw new InvalidUserException();
		}
		
		//Update the users password if a newer PasswordBuilder
		if(!PasswordBuilderFactory.isNewest(rs.getString("password_version"))){
			//TODO change password
		}
		
		//TODO the stuff
		
		return null;
	}
	
	public SessionInformation register(String firstName, String lastName, String email, String password){
		return null;
	}
	
	/**
	 * Checks if a user's password is strong enough for their UserType
	 * @param score The password score from {@link passwordStrength(String)}
	 * @param userType The user's type
	 * @return If the users password is strong enough
	 */
	public boolean isPasswordStrongEnough(int score, Protobuf.UserType userType){
		return (userType == Protobuf.UserType.USER && score >= 1) ||
				(userType == Protobuf.UserType.MOD && score >= 2) ||
				(userType == Protobuf.UserType.ADMIN && score == 4);
	}
	
	/**
	 * Checks the inputted password's strength
	 * @param password The password to check
	 * @return The inputted password's strength
	 */
	public int passwordStrength(String password){
		Zxcvbn zxcvbn = new Zxcvbn();
		Strength strength = zxcvbn.measure(password);
		return strength.getScore();
	}
	
	/**
	 * Returns the information for the provided serialized sessionsignature message
	 * @param serialized A serialized SessionSignature object
	 * @return A ShortUser. This data can be trusted
	 * @throws InvalidProtocolBufferException The provided protocol buffer is not a valid sessionsignature message
	 * @throws InvalidSessionException The session is invalid
	 */
	public ShortUser getSessionUser(byte[] serialized) throws InvalidProtocolBufferException, InvalidSessionException{
		Protobuf.SessionSignature si = Protobuf.SessionSignature.parseFrom(serialized);
		return getSessionUser(new SessionSignature(si));
	}
	
	/**
	 * Returns the information for the provided SessionSignature message
	 * @param ss A SessionSignature
	 * @return A ShortUser. This data can be trusted
	 * @throws InvalidSessionException
	 */
	public ShortUser getSessionUser(SessionSignature ss) throws InvalidSessionException{
		Protobuf.Session s = null;
		
		try{
			s = decrypt(ss).getBase();
		}catch(Exception e){
			throw new InvalidSessionException("The provided session was invalid", e);
		}
		
		return null;
	}
	
	/**
	 * Generates a securely random salt for use with passwords
	 * @return
	 */
	public String generateSalt(){
		byte[] salt = new byte[16];
		context.getRandom().nextBytes(salt);
		return new String(salt, StandardCharsets.UTF_8);	
	}
	
	/**
	 * Encrypts a Session and returns it with a signature
	 * @param session The session to encrypt
	 * @return An encrypted Session and it's signature
	 */
	public static SessionSignature encrypt(Session session) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		Key key = generateKey();
		Cipher c = Cipher.getInstance(AuthManagerKey.ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		
		Protobuf.Session s = session.getBase();
		Protobuf.SessionSignature.Builder ssb = Protobuf.SessionSignature.newBuilder();
		//Encrypt the session
		ssb.setSession(ByteString.copyFrom(c.doFinal(s.toByteArray())));
		//Generate a signature
		ssb.setSignature(Utils.sha256(s.toByteArray()));
		
		return new SessionSignature(ssb.build());
	}
	
	/**
	 * Decrypts and validates a SessionSignature
	 * @param sessionSignature The SessionSignature to decrypt
	 * @return The Session
	 * @throws IntegrityBrokenException The session's integrity has been broken
	 */
	public static Session decrypt(SessionSignature sessionSignature) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidProtocolBufferException, IllegalBlockSizeException, BadPaddingException, IntegrityBrokenException{
		Key key = generateKey();
		Cipher c = Cipher.getInstance(AuthManagerKey.ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		
		//Decrypt and parse the session
		Protobuf.Session session = Protobuf.Session.parseFrom(c.doFinal(sessionSignature.getBase().getSession().toByteArray()));
		//Verify the session integrity
		if(!verifyIntegrity(session, sessionSignature.getBase().getSignature())){
			throw new IntegrityBrokenException("The session has been modified or corrupted");
		}
		
		return new Session(session);
	}
	
	/**
	 * Verify a session's integrity
	 * @param session The session to verify
	 * @param signature The signature of the session
	 * @return If the session is integral
	 */
	public static boolean verifyIntegrity(Protobuf.Session session, String signature){
		return Utils.sha256(session.toByteArray()).equals(signature);
	}
	
	/**
	 * Generates an encryption key
	 * @return The encryption key
	 */
	private static Key generateKey(){
		return new SecretKeySpec(AuthManagerKey.key, AuthManagerKey.ALGO);
	}
}
