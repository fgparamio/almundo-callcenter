package com.almundo.callcenter.service;

import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.almundo.callcenter.domain.Employee;

/**
 * Almundo Callcenter PriorityBlockingQueue Employee Manager Service
 * @author fgparamio
 *
 */
@Service
final public class EmployeeService {
	
	// Private slf4j Logger
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);
	
	// Priority Queue. Needs Employee implements Comparable
	private PriorityBlockingQueue<Employee> blockingQueue = new PriorityBlockingQueue<Employee>();
	
	/**
	 * Inserts the specified element into this priority queue
	 * 
	 * @param employee
	 * @throws InterruptedException
	 */
	public void putEmployee(final Employee employee) throws InterruptedException {
		LOG.debug("Putting employee into Queue: " + employee.toString());
		blockingQueue.put(employee);
	}
	
	/**
	 * Retrieves, but does not remove, the head of this queue
	 * 
	 * @return
	 */
	public Employee peekEmployee() {
		LOG.debug("Peek Head Employee in Queue");
		return blockingQueue.peek();		
	}
	
	/**
	 *  Retrieves and removes the head of queue, waiting if necessary until an element becomes available.
	 *  
	 * @return 
	 * @throws InterruptedException
	 */
	public Employee takeEmployee() throws InterruptedException {
		final Employee employee = blockingQueue.take();
		LOG.debug("Take Head Employee in Queue:" + employee.toString());
		return employee;
	}
	
	/**
	 * Atomically removes all of the elements from this queue. The queue will be empty after this call returns.
	 */
	public void cleanEmployes() {
		LOG.debug("Clean All Employes in Queue");
		this.blockingQueue.clear();
	}
		
}
