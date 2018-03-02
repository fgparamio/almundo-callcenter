package com.almundo.callcenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 	 
 * Almundo CallCenter 
 * Abstract Employee parent Class
 * @author fgparamio
 *
 */
public abstract class Employee implements Comparable<Employee> {

	private String name;
	
	@JsonIgnore
	private PriorityType priorityType;
	
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
	public Employee(final String name, final PriorityType type) {
		this.name = name;
		this.priorityType = type;
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
	 * @return priority employee Type (OPERATOR, SUPERVISOR OR DIRECTOR)
	 */
	public PriorityType getPriorityType() {
		return priorityType;
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
	 * @param priority type of employee
	 */
	public void setPriorityType(final PriorityType priorityType) {
		this.priorityType = priorityType;
	}
	
	/**
	 * 
	 * @return priority of employee
	 */
	public Integer getPriority() {
		return this.priorityType.getPriority();
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
		return "Employee [name=" + name + ", type=" + priorityType + "]";
	}
}
