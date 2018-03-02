package com.almundo.callcenter.exception;

/**
 * Almundo CallCenter API Resource Exception
 * For HTTP 404 errros
 */
public class ResourceNotFoundException extends RuntimeException {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -423953267754564429L;

	/**
	 * Default Constructor
	 */
    public ResourceNotFoundException() {
        super();
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     */
    public ResourceNotFoundException(final Throwable cause) {
        super(cause);
    }
}
