package com.almundo.callcenter.exception;

/**
 * Almundo CallCenter
 * for HTTP 400 errors
 */
public class NotFreeEmployeesException extends RuntimeException {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8866484021939618274L;

	/**
	 * Default Constructor 
	 */
    public NotFreeEmployeesException() {
        super();
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public NotFreeEmployeesException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     */
    public NotFreeEmployeesException(final String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     */
    public NotFreeEmployeesException(final Throwable cause) {
        super(cause);
    }

}
