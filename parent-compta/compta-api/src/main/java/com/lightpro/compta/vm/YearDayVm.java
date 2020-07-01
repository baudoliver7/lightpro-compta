package com.lightpro.compta.vm;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonGetter;

public final class YearDayVm {
	private final transient int month;
	private final transient int day;
	
	public YearDayVm(){
		throw new UnsupportedOperationException("#YearDayVm()");
	}
		
	public YearDayVm(int month, int day){
		this.month = month;
		this.day = day;
	}
	
	@JsonGetter
	public int day(){
		return day;
	}
	
	@JsonGetter
	public int month() throws IOException {
		return month;
	}
}
