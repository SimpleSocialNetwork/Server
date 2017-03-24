package com.arctro.ssn.backend.controllers;

import com.arctro.ssn.protobuf.models.ProtobufCollectionWrapper;
import com.arctro.ssn.protobuf.models.ServableProtobufWrapper;
import com.arctro.ssn.supporting.SSNContext;

public abstract class ProtobufController<S extends ServableProtobufWrapper<?>, C extends ProtobufCollectionWrapper<?>> {
	
	SSNContext context;
	
	public ProtobufController(SSNContext context){
		this.context = context;
	}
	
	
	public abstract S get(int id);
	
	public abstract void set(S proto);
	
	public abstract void delete(int id);
	
	public abstract ResourceAccessibility isAccessible(int id);
}
