package com.lightpro.compta.cmd;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrameEdited {
	private final List<TrameFluxEdited> fluxes;
	private final String name;
	
	public TrameEdited(){
		throw new UnsupportedOperationException("#TrameEdited()");
	}
	
	@JsonCreator
	public TrameEdited(@JsonProperty("name") final String name, @JsonProperty("fluxes") final List<TrameFluxEdited> fluxes){
		
		this.fluxes = fluxes;
		this.name = name;
	}
	
	public String name(){
		return name;
	}
	
	public List<TrameFluxEdited> fluxes(){
		return fluxes;
	}
}
