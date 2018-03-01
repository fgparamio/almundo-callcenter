package com.almundo.callcenter.service;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.almundo.callcenter.domain.Employee;
import com.almundo.callcenter.exception.BusyConcurrentException;

/**
 * 
 * @author fgparamio
 *
 */
@Component
public class Dispatcher {

	private static final Logger LOG = LoggerFactory.getLogger(Dispatcher.class);

	@Value("${callcenter.minCallTime}")
	private Integer minCallTime;
	
	@Value("${callcenter.maxCallTime}")
	private Integer maxCallTime;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPool;

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 
	 * @param message
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Future<String> dispatchCall(final String message,final boolean wait) throws InterruptedException, ExecutionException {

		if (!wait && (isPoolMaxConcurrent() || notFreeEmployees())) {
			throw new BusyConcurrentException();
		}

		return threadPool.submit(new EmployeeTask(message));
	}

	/**
	 * Callable for ThreadPoolExecutar Tasks
	 * 
	 * @author fgparamio
	 *
	 */
	public class EmployeeTask implements Callable<String> {

		private String message;

		public EmployeeTask(final String message) {
			this.message = message;
		}

		@Override
		public String call() throws Exception {
			
			// Get free employee
			final Employee employee = employeeService.takeEmployee();
			// Process Call
			Thread.sleep(getRandomNumberInRange(minCallTime, maxCallTime) * 1000);
			// Put new free employee (same employee)
			employeeService.putEmployee(employee);
			// Get String result to logger
			String result = new StringBuilder().append("Employee name: ").append(employee.getName())
					.append(" Responding message:").append(message).toString();
			LOG.info(result);
			return result;
		}
	}

	/**
	 * 
	 * @return true if not free employees. False otherwise
	 */
	private boolean notFreeEmployees() {
		return employeeService.peekEmployee() == null;
	}

	/**
	 * 
	 * @return true if Pool is busy. False otherwise
	 */
	private boolean isPoolMaxConcurrent() {
		return threadPool.getActiveCount() >= threadPool.getMaxPoolSize();
	}

	/**
	 * 	Get random number from min-max range
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	private static int getRandomNumberInRange(int min, int max) {
		Random random = new Random();
		return random.ints(min, (max + 1)).findFirst().getAsInt();
	}
}
