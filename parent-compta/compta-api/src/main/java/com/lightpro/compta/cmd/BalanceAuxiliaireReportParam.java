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

public class BalanceAuxiliaireReportParam {
	
	private final String format;
	private final Period period;
	private final UUID tiersTypeId;
	
	public BalanceAuxiliaireReportParam(){
		throw new UnsupportedOperationException("#BalanceAuxiliaireReportParam()");
	}
	
	@JsonCreator
	public BalanceAuxiliaireReportParam(@JsonProperty("start") final Date start,
			@JsonProperty("end") final Date end,
			@JsonProperty("tiersTypeId") final UUID tiersTypeId,
			@JsonProperty("format") final String format){
				
		LocalDate startL = TimeConvert.toLocalDate(start, ZoneId.systemDefault());
		LocalDate endL = TimeConvert.toLocalDate(end, ZoneId.systemDefault());
		this.period = new PeriodBase(startL, endL);
		this.tiersTypeId = tiersTypeId;
		this.format = format;
	}
	
	public Period period(){
		return period;
	}
	
	public UUID tiersTypeId(){
		return tiersTypeId;
	}
	
	public String format(){
		return format;
	}
}
