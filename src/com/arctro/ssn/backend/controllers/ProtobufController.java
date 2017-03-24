package com.arctro.ssn.backend.controllers;

import com.arctro.ssn.protobuf.models.ProtobufWrapper;
import com.arctro.ssn.supporting.SSNContext;

public abstract class ProtobufController<T extends ProtobufWrapper<?>> {
	
	SSNContext context;
	
	public ProtobufController(SSNContext context){
		this.context = context;
	}
	
}
