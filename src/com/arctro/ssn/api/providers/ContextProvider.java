package com.arctro.ssn.api.providers;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import com.arctro.ssn.supporting.SSNContext;

@Provider
public class ContextProvider implements ContainerRequestFilter{
	@Override
	public void filter(ContainerRequestContext crc) throws IOException {
		
		crc.setProperty(SSNContext.CONTEXT_PROPERTY, new SSNContext());
	}

}
