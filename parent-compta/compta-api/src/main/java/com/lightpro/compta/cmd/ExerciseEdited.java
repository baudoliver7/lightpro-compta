package com.lightpro.compta.cmd;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.common.utilities.convert.TimeConvert;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExerciseEdited {
	
	private final LocalDate start;
	private final LocalDate end;
	
	public ExerciseEdited(){
		throw new UnsupportedOperationException("#ExerciseEdited()");
	}
	
	@JsonCreator
	public ExerciseEdited( @JsonProperty("start") final Date start, 
				    	  @JsonProperty("end") final Date end){
		
		this.start = TimeConvert.toLocalDate(start, ZoneId.systemDefault());
		this.end = TimeConvert.toLocalDate(end, ZoneId.systemDefault());
	}
	
	public LocalDate start(){
		return start;
	}
	
	public LocalDate end(){
		return end;
	}
}
