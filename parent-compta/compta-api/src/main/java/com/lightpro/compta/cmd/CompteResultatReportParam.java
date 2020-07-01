package com.lightpro.compta.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CompteResultatReportParam {
	
	private final String format;
	private final UUID exerciseId;
	
	public CompteResultatReportParam(){
		throw new UnsupportedOperationException("#CompteResultatReportParam()");
	}
	
	@JsonCreator
	public CompteResultatReportParam(@JsonProperty("exerciseId") final UUID exerciseId,
			@JsonProperty("format") final String format){
				
		this.exerciseId = exerciseId;
		this.format = format;
	}
	
	public UUID exerciseId(){
		return exerciseId;
	}
	
	public String format(){
		return format;
	}
}
