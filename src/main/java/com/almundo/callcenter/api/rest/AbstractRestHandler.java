package com.almundo.callcenter.api.rest;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.almundo.callcenter.domain.RestErrorInfo;
import com.almundo.callcenter.exception.BusyConcurrentException;
import com.almundo.callcenter.exception.NotFreeEmployeesException;
import com.almundo.callcenter.exception.ResourceNotFoundException;

/**
 * Almundo CallCenter
 * 
 * @author fgparamio
 * 
 * This class is meant to be extended by all REST resource "controllers". It
 * contains exception mapping and other common REST API functionality
 */
public abstract class AbstractRestHandler implements ApplicationEventPublisherAware {

	// Private slf4j Logger
	private static final Logger LOG = LoggerFactory.getLogger(AbstractRestHandler.class);
	// Spring Event Publisher
	protected ApplicationEventPublisher eventPublisher;
	
	/**
	 * 
	 * @param ex when free employees not exist
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NotFreeEmployeesException.class)
	public @ResponseBody RestErrorInfo handleDataStoreException(NotFreeEmployeesException ex, WebRequest request,
			HttpServletResponse response) {
		LOG.debug("Converting Data Store exception to RestResponse : " + ex.getMessage());
		return new RestErrorInfo(ex, "Not free employees to attempt calls");
	}


	/**
	 * 
	 * @param ex when Threads Pool is busy 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusyConcurrentException.class)
	public @ResponseBody RestErrorInfo handleDataStoreException(BusyConcurrentException ex, WebRequest request,
			HttpServletResponse response) {
		LOG.debug("Converting Data Store exception to RestResponse : " + ex.getMessage());
		return new RestErrorInfo(ex, "PoolMaxConcurrent is busy and not wait");
	}

	/**
	 * 
	 * @param ex When Resource Rest not found 
	 * @param request 
	 * @param response
	 * @return
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public @ResponseBody RestErrorInfo handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request,
			HttpServletResponse response) {
		LOG.info("ResourceNotFoundException handler:" + ex.getMessage());
		return new RestErrorInfo(ex, "Sorry I couldn't find it.");
	}

	/**
	 * set current EventPublisher <= Spring Context
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

	/**
	 * 
	 * @param resource
	 * @return
	 * @throws ResourceNotFoundException if resource not exist
	 */
	public static <T> T checkResourceFound(final T resource) {
		if (resource == null) {
			throw new ResourceNotFoundException("resource not found");
		}
		return resource;
	}

}