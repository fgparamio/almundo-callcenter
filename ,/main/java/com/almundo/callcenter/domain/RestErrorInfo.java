package com.almundo.callcenter.domain;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * A sample class for adding error information in the response
 */
/**
 * 
 * @author fgparamio
 *
 */
@XmlRootElement
public class RestErrorInfo {
    public final String detail;
    public final String message;

    public RestErrorInfo(final Exception ex, final String detail) {
        this.message = ex.getLocalizedMessage();
        this.detail = detail;
    }
}
