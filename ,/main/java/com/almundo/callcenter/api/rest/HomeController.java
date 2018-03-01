package com.almundo.callcenter.api.rest;

import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.almundo.callcenter.domain.Director;
import com.almundo.callcenter.domain.Operator;
import com.almundo.callcenter.domain.Supervisor;
import com.almundo.callcenter.service.Dispatcher;
import com.almundo.callcenter.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */
/**
 * 
 * @author fgparamio
 *
 */
@RestController
@RequestMapping(value = "/almundo/v1/callcenter")
@Api(tags = { "employees" })
public class HomeController extends AbstractRestHandler {


	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private Dispatcher dispatcher;

	/**
	 * 
	 * @param operator
	 * @param request
	 * @param response
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "/operator", method = RequestMethod.POST, consumes = { "application/json",
			"application/xml" }, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create a employee resource")
	public void createOperator(@RequestBody Operator operator, HttpServletRequest request, HttpServletResponse response)
			throws InterruptedException {
		this.employeeService.putEmployee(operator);
	}

	/**
	 * 
	 * @param supervisor
	 * @param request
	 * @param response
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "/supervisor", method = RequestMethod.POST, consumes = { "application/json",
			"application/xml" }, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create a employee resource")
	public void createSupervisor(@RequestBody Supervisor supervisor, HttpServletRequest request,
			HttpServletResponse response) throws InterruptedException {
		this.employeeService.putEmployee(supervisor);
	}

	/**
	 * 
	 * @param director
	 * @param request
	 * @param response
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "/director", method = RequestMethod.POST, consumes = { "application/json",
			"application/xml" }, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create a employee resource")
	public void createDirector(@RequestBody Director director, HttpServletRequest request, HttpServletResponse response)
			throws InterruptedException {
		this.employeeService.putEmployee(director);
	}

	/**
	 * 
	 * @param message
	 * @param isWait
	 * @param request
	 * @param response
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@RequestMapping(value = "/call", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create a employee resource")
	public void createCall(@RequestParam("message") String message, @RequestParam(name = "isWait", defaultValue = "false") Boolean isWait,
			HttpServletRequest request, HttpServletResponse response) throws InterruptedException, ExecutionException {
		this.dispatcher.dispatchCall(message, isWait);
	}

}
