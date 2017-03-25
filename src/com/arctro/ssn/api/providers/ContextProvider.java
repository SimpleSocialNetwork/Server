package com.arctro.ssn.api.providers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import com.arctro.ssn.backend.auth.AuthManager;
import com.arctro.ssn.supporting.Const;
import com.arctro.ssn.supporting.SSNContext;
import com.arctro.ssn.supporting.exceptions.InvalidSessionException;

@Priority(1)
@Provider
public class ContextProvider implements ContainerRequestFilter{
	@Override
	public void filter(ContainerRequestContext crc) throws IOException {
		SSNContext c = new SSNContext();
		
		AuthManager authManager = new AuthManager(null);
		try {
			c.setUser(authManager.getSessionUser(crc.getHeaderString(Const.AUTH_HEADER).getBytes(StandardCharsets.UTF_8)));
		} catch (InvalidSessionException e) {
			e.printStackTrace();
		}
		
		crc.setProperty(SSNContext.CONTEXT_PROPERTY, c);
	}

}
