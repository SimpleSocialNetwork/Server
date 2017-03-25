package com.arctro.ssn.backend.controllers;

import com.arctro.ssn.protobuf.models.ProtobufCollectionWrapper;

/**
 * An interface that implements searchability into ProtobufControllers
 * @author Ben McLean
 *
 * @param <C> The collection type of the concrete ProtobufController
 */
public interface SearchableProtobufController<C extends ProtobufCollectionWrapper<?>> {
	
	/**
	 * Executes a search of the query
	 * @param query The search query
	 * @param page The page of results
	 * @return The search results
	 */
	public C search(String query, int page);

}
