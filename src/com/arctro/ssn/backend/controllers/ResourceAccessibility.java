package com.arctro.ssn.backend.controllers;

/**
 * The accessibility of a resource
 * @author Ben McLean
 */
public enum ResourceAccessibility {
	/**
	 * The resource can be edited
	 */
	EDIT,
	
	/**
	 * The resource can be viewed
	 */
	VIEW,
	
	/**
	 * The resource cannot be viewed or edited
	 */
	NONE
}
