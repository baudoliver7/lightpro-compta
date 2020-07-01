package com.lightpro.compta.cmd;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.common.utilities.convert.TimeConvert;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infrastructure.core.Period;
import com.infrastructure.core.impl.PeriodBase;

public class BalanceGeneraleReportParam {
	
	private final String format;
	private final Period period;
	
	public BalanceGeneraleReportParam(){
		throw new UnsupportedOperationException("#BalanceGeneraleReportParam()");
	}
	
	@JsonCreator
	public BalanceGeneraleReportParam(@JsonProperty("start") final Date start,
			@JsonProperty("end") final Date end,
			@JsonProperty("format") final String format){
				
		LocalDate startL = TimeConvert.toLocalDate(start, ZoneId.systemDefault());
		LocalDate endL = TimeConvert.toLocalDate(end, ZoneId.systemDefault());
		this.period = new PeriodBase(startL, endL);
		this.format = format;
	}
	
	public Period period(){
		return period;
	}
	
	public String format(){
		return format;
	}
}
