package com.lightpro.compta.cmd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrameParamDetailCmd {
	
	private final String name;
	private final double value;
	
	public TrameParamDetailCmd(){
		throw new UnsupportedOperationException("#TrameParamDetailCmd()");
	}
	
	@JsonCreator
	public TrameParamDetailCmd(@JsonProperty("name") final String name,
							   @JsonProperty("value") final double value){
		
		this.name = name;		
		this.value = value;
	}
	
	public String name(){
		return name;
	}
	
	public double value(){
		return value;
	}
}
