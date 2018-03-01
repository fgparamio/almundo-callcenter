package com.almundo.callcenter.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * 
 * @author fgparamio
 *
 */
@JsonDeserialize(as = Operator.class)
public class Operator extends Employee {
	
	public Operator() {
		super();
	}
	public Operator(final String name) {
		super(name,"OPERATOR",1);
	}		
}
