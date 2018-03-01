package com.almundo.callcenter.service;

import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.stereotype.Service;

import com.almundo.callcenter.domain.Employee;

/**
 * 
 * @author Entelgy
 *
 */
@Service
public class EmployeeService {
	
	// Priority Queue. Needs Employee implements Comparable
	private PriorityBlockingQueue<Employee> blockingQueue = new PriorityBlockingQueue<Employee>();
	
	/**
	 * 
	 * @param employee
	 * @throws InterruptedException
	 */
	public void putEmployee(final Employee employee) throws InterruptedException {
		blockingQueue.put(employee);
	}
	
	/**
	 * 
	 * @return
	 */
	public Employee peekEmployee() {
		return blockingQueue.peek();		
	}
	
	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public Employee takeEmployee() throws InterruptedException {
		return blockingQueue.take();
	}
	
	/**
	 * 
	 */
	public void cleanEmployes() {
		this.blockingQueue.clear();
	}
		
}
