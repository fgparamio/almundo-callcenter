package com.almundo.callcenter.domain;

/**
 * Almundo CallCenter Supervisor DTO 
 * @author fgparamio
 *
 */
public class Supervisor extends Employee {

	/**
	 * Default Constructor
	 */
	public Supervisor() {
		super();
	}
	
	/**
	 * 
	 * @param name of supervisor employee
	 */
	public Supervisor(final String name) {
		super(name,EmployeeType.SUPERVISOR);
	}
}
