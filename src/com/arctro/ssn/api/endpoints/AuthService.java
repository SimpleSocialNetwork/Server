package com.arctro.ssn.api.endpoints;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.arctro.ssn.backend.auth.PasswordValidator;
import com.arctro.ssn.protobuf.Protobuf;

@Path("/auth")
public class AuthService {
	
	@Path("/password/strength")
	@POST
	@Produces("application/protobuf+PasswordStrength")
	public Response getPasswordStrength(@Context ContainerRequestContext crc, @FormParam("password") String password, @FormParam("type") Integer type){
		PasswordValidator passwordValidator = null;
		
		if(type == null){
			passwordValidator = new PasswordValidator();
		}else{
			passwordValidator = new PasswordValidator(Protobuf.UserType.forNumber(type));
		}
		
		Protobuf.PasswordStrength.Builder psb = Protobuf.PasswordStrength.newBuilder();
		psb.setStrength(passwordValidator.getScore(password));
		psb.setStrongEnough(passwordValidator.isValid(password));
		
		return Response.status(200).entity(psb.build().toByteArray()).build();
	}
}
