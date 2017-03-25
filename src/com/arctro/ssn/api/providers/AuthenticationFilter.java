package com.arctro.ssn.api.providers;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

/**
 * Filters unauthenticated users
 * @author Ben McLean
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
@Authenticated
public class AuthenticationFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext crc) throws IOException {
		
	}

}
