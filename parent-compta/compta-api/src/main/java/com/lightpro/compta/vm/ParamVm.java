package com.lightpro.compta.vm;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonGetter;

public final class ParamVm {
	
	private final transient String name;
	private final transient Double value;
	
	public ParamVm(){
		throw new UnsupportedOperationException("#ParamVm()");
	}
		
	public ParamVm(final String name, final Double value){
		this.name = name;
		this.value = value;
	}
	
	@JsonGetter
	public String name() throws IOException {
		return name;
	}
	
	@JsonGetter
	public Double value(){
		return value;
	}		
}
