package com.arctro.ssn.backend.controllers;

import com.arctro.ssn.protobuf.models.ProtobufCollectionWrapper;
import com.arctro.ssn.protobuf.models.ProtobufWrapper;
import com.arctro.ssn.supporting.SSNContext;

/**
 * A common ancestor for ProtobufControllers
 * @author Ben McLean
 *
 * @param <S> The ProtobufWrapper that this controller controls
 * @param <C> The Collection type of S
 */
public abstract class ProtobufController<S extends ProtobufWrapper<?>, C extends ProtobufCollectionWrapper<?>>  {
	
	SSNContext context;
	
	/**
	 * Initializes the ProtobufController
	 * @param context The request's SSNContext
	 */
	public ProtobufController(SSNContext context){
		this.context = context;
	}
	
}
