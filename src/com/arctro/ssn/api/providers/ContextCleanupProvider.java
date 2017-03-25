package com.arctro.ssn.api.providers;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import com.arctro.ssn.supporting.SSNContext;

/**
 * Cleans up after an endpoint is executed
 * @author Ben McLean
 */
@Provider
public class ContextCleanupProvider implements ContainerResponseFilter{

	@Override
	public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
		SSNContext context = (SSNContext) req.getProperty(SSNContext.CONTEXT_PROPERTY);
		context.close();
	}

}
