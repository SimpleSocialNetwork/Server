package com.arctro.ssn.supporting;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import com.arctro.ssn.protobuf.models.impl.ShortUser;

/**
 * Holds a request's context and common resources
 * @author Ben McLean
 */
public class SSNContext {
	/**
	 * The property name of SSNContext in a ContainerRequestContext
	 * @see ContainerRequestContext
	 */
	public static final String CONTEXT_PROPERTY = "_context";
	
	ShortUser user;
	Connection connection;
	Random random;
	
	public SSNContext(){}
	
	/**
	 * Returns the authenticated user, or null if the user is unauthenticated
	 * @return The authenticated user, or null if the user is unauthenticated
	 */
	public ShortUser getUser() {
		return user;
	}

	/**
	 * Sets the authenticated user
	 * @param user The authenticated user
	 */
	public void setUser(ShortUser user) {
		this.user = user;
	}
	
	/**
	 * Returns a connection to the MySQL database
	 * @return A connection to the MySQL database
	 */
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Returns a common instance of random
	 * @return A common instance of random
	 */
	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	/**
	 * Cleans up the Context
	 */
	public void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
