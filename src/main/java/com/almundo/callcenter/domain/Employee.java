package com.almundo.callcenter.domain;

/**
 * 	 
 * Almundo CallCenter 
 * Abstract Employee parent Class
 * @author fgparamio
 *
 */
public abstract class Employee implements Comparable<Employee> {

	private String name;
	private EmployeeType type;
	
	/**
	 * Default Constructor
	 */
	public Employee() {
		super();
	}
	
	/**
	 * 
	 * @param name of employee
	 * @param type of employee 
	 * @param priority
	 */
	public Employee(final String name, final EmployeeType type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * 
	 * @param name
	 */
	public Employee (final String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return employee Type (OPERATOR, SUPERVISOR OR DIRECTOR)
	 */
	public EmployeeType getType() {
		return type;
	}

	/**
	 * 
	 * @return employee name
	 */
	public String getName() {
		return this.name;
	}
	
	
	/**
	 * 
	 * @param name of employee
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param type of employee
	 */
	public void setType(final EmployeeType type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @return priority of employee
	 */
	public Integer getPriority() {
		return this.type.getPriority();
	}

	
	/**
	 *  For Priority queue by priority number (1 > 3)
	 */
	@Override
	public int compareTo(final Employee employee) {
		return this.getPriority().compareTo(employee.getPriority());
	}

	/**
	 * String employee rendering
	 */
	@Override
	public String toString() {
		return "Employee [name=" + name + ", type=" + type + "]";
	}
}
