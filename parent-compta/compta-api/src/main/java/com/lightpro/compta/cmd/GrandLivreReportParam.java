package com.lightpro.compta.cmd;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import com.common.utilities.convert.TimeConvert;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infrastructure.core.Period;
import com.infrastructure.core.impl.PeriodBase;

public class GrandLivreReportParam {
	
	private final String format;
	private final Period period;
	private final UUID journalId;
	
	public GrandLivreReportParam(){
		throw new UnsupportedOperationException("#GrandLivreReportParam()");
	}
	
	@JsonCreator
	public GrandLivreReportParam(@JsonProperty("start") final Date start,
			@JsonProperty("end") final Date end,
			@JsonProperty("journalId") final UUID journalId,
			@JsonProperty("format") final String format){
				
		LocalDate startL = TimeConvert.toLocalDate(start, ZoneId.systemDefault());
		LocalDate endL = TimeConvert.toLocalDate(end, ZoneId.systemDefault());
		this.period = new PeriodBase(startL, endL);
		this.journalId = journalId;
		this.format = format;
	}
	
	public Period period(){
		return period;
	}
	
	public UUID journalId(){
		return journalId;
	}
	
	public String format(){
		return format;
	}
}
