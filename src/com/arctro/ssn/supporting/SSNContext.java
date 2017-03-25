package com.arctro.ssn.supporting;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import com.arctro.ssn.protobuf.models.impl.ShortUser;

public class SSNContext {
	public static final String CONTEXT_PROPERTY = "_context";
	
	ShortUser user;
	Connection connection;
	Random random;
	
	public SSNContext(){}
	
	public ShortUser getUser() {
		return user;
	}

	public void setUser(ShortUser user) {
		this.user = user;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
