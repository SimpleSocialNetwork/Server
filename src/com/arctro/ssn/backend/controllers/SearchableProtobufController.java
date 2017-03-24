package com.arctro.ssn.backend.controllers;

import com.arctro.ssn.protobuf.models.ProtobufCollectionWrapper;

public interface SearchableProtobufController<C extends ProtobufCollectionWrapper<?>> {
	
	public C search(String query, int page);

}
