package com.arctro.ssn.backend.auth;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.arctro.ssn.protobuf.Protobuf;
import com.arctro.ssn.protobuf.models.impl.Session;
import com.arctro.ssn.protobuf.models.impl.SessionSignature;
import com.arctro.ssn.supporting.Utils;
import com.arctro.ssn.supporting.exceptions.IntegrityBrokenException;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class AuthManager {
	
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
