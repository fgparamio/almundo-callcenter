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
		super();
	}
	
	/**
	 * 
	 * @param name of director employee
	 */
	public Director(final String name) {
		super(name,EmployeeType.DIRECTOR);
	}
}
