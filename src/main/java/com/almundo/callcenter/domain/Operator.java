package com.almundo.callcenter.domain;

/**
 * Almundo CallCenter Operator DTO 
 * @author fgparamio
 *
 */
public class Operator extends Employee {
	
	/**
	 * Default Constructor
	 */
	public Operator() {
		super();
	}
	
	/**
	 * 
	 * @param name of operator employee
	 */
	public Operator(final String name) {
		super(name,EmployeeType.OPERATOR);
	}		
}
