package com.lightpro.compta.cmd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class YearDayEdited {
	private final int day;
	private final int month;
	
	public YearDayEdited(){
		throw new UnsupportedOperationException("#YearDayEdited()");
	}
	
	@JsonCreator
	public YearDayEdited( @JsonProperty("day") final int day, 
				    	  @JsonProperty("month") final int month){
		
		this.day = day;
		this.month = month;
	}
	
	public int day(){
		return day;
	}
	
	public int month(){
		return month;
	}
}
