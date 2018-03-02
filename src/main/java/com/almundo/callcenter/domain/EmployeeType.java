package com.almundo.callcenter.domain;

/**
 * 	Almundo Callcenter 
 * 
 *   Type of Employees
 *  
 * @author fgparamio
 *
 */
public enum EmployeeType {

	OPERATOR(1),   // the highest priority
	SUPERVISOR(2), // the medium priority
	DIRECTOR(3);   // the lowest priority 
	
	// Priority of employee
	private final Integer priority;
	
	/**
	 * 
	 * @param priority Constructor
	 */
	EmployeeType(Integer priority) {
		this.priority = priority;
	}
	
	/**
	 * 
	 * @return priority of Employee
	 */
	public Integer getPriority() {
		return this.priority;
	}
}
