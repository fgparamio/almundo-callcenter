package com.almundo.callcenter.domain;

/**
 * Almundo CallCenter Director DTO
 * @author fgparamio
 *
 */
public class Director extends Employee {
	
	/**
	 * Default Constructor 
	 */
	public Director() {
		super("DEFAULT_DIRECTOR", PriorityType.OPERATOR);
	}
	
	/**
	 * 
	 * @param name of director employee
	 */
	public Director(final String name) {
		super(name,PriorityType.DIRECTOR);
	}
}
