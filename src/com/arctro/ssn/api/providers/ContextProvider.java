package com.arctro.ssn.api.providers;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import com.arctro.ssn.supporting.Context;

@Provider
public class ContextProvider implements ContainerRequestFilter{
	@Override
	public void filter(ContainerRequestContext crc) throws IOException {
		crc.setProperty(Context.CONTEXT_PROPERTY, new Context());
	}

}
