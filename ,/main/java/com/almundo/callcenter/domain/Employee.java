package com.almundo.callcenter.domain;

/**
 * 
 * @author fgparamio
 *
 */
public abstract class Employee implements Comparable<Employee> {

	protected String name;
	protected String type;
	protected Integer priority;
	
	public Employee() {
		super();
	}
	
	public Employee(final String name, final String type, final Integer priority) {
		this.name = name;
		this.type = type;
		this.priority = priority;
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
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	
	/**
	 * 
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * 
	 * @param priority
	 */
	public void setPriority(final Integer priority) {
		this.priority = priority;
	}

	/**
	 *  For Priority queue by priority number (1 > 3)
	 */
	@Override
	public int compareTo(final Employee employee) {
		return this.getPriority().compareTo(employee.getPriority());
	}	
}
