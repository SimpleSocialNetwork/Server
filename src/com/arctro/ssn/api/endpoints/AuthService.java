package com.arctro.ssn.api.endpoints;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.arctro.ssn.backend.auth.AuthManager;
import com.arctro.ssn.protobuf.Protobuf;
import com.arctro.ssn.supporting.SSNContext;

@Path("/auth")
public class AuthService {
	
	@Path("/password/strength")
	@POST
	@Produces("application/protobuf+PasswordStrength")
	public Response getPasswordStrength(@Context ContainerRequestContext crc, @FormParam("password") String password, @FormParam("type") Integer type){
		AuthManager authManager = new AuthManager((SSNContext) crc.getProperty(SSNContext.CONTEXT_PROPERTY));
		
		int strength = authManager.passwordStrength(password);
		boolean strongEnough = false;
		
		if(type == null){
			strongEnough = authManager.isPasswordStrongEnough(strength, Protobuf.UserType.USER);
		}else{
			strongEnough = authManager.isPasswordStrongEnough(strength, Protobuf.UserType.forNumber(type));
		}
		
		Protobuf.PasswordStrength.Builder psb = Protobuf.PasswordStrength.newBuilder();
		psb.setStrength(strength);
		psb.setStrongEnough(strongEnough);
		
		return Response.status(200).entity(psb.build().toByteArray()).build();
	}
}
