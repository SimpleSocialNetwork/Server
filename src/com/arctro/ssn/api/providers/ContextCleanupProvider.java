package com.arctro.ssn.api.providers;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import com.arctro.ssn.supporting.Context;

@Provider
public class ContextCleanupProvider implements ContainerResponseFilter{

	@Override
	public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
		Context context = (Context) req.getProperty(Context.CONTEXT_PROPERTY);
		context.close();
	}

}
