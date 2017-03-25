package com.arctro.ssn.backend.controllers;

import com.arctro.ssn.protobuf.models.ProtobufCollectionWrapper;
import com.arctro.ssn.protobuf.models.ServableProtobufWrapper;
import com.arctro.ssn.supporting.SSNContext;

/**
 * A common ancestor for most ProtobufControllers
 * @author Ben McLean
 *
 * @param <S> The ServableProtobufWrapper that this controller controls
 * @param <C> The Collection type of S
 */
public abstract class ProtobufController<S extends ServableProtobufWrapper<?>, C extends ProtobufCollectionWrapper<?>> {
	
	SSNContext context;
	
	/**
	 * Initializes the ProtobufController
	 * @param context The request's SSNContext
	 */
	public ProtobufController(SSNContext context){
		this.context = context;
	}
	
	/**
	 * Returns the S associated with the provided ID
	 * @param id The ID of the S to get
	 * @return The S associated with the provided ID
	 */
	public abstract S get(int id);
	
	/**
	 * Sets/creates an S
	 * @param proto The S to create/set
	 */
	public abstract void set(S proto);
	
	/**
	 * Deletes the S associated with the provided ID
	 * @param id The ID of the S to delete
	 */
	public abstract void delete(int id);
	
	/**
	 * Checks if an S is accessible
	 * @param id The ID of the S to check
	 * @return If an S is accessible
	 */
	public abstract ResourceAccessibility isAccessible(int id);
}
