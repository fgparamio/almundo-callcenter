package com.almundo.callcenter.domain;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Almundo CallCenter
 *  
 *  Rest Error info DTO
 *  
 * @author fgparamio
 *
 */
@XmlRootElement
public class RestErrorInfo {
	
	// Error Detail
    public final String detail;
    // Error Message
    public final String message;

    /**
     * 
     * @param ex
     * @param detail
     */
    public RestErrorInfo(final Exception ex, final String detail) {
        this.message = ex.getLocalizedMessage();
        this.detail = detail;
    }
}
