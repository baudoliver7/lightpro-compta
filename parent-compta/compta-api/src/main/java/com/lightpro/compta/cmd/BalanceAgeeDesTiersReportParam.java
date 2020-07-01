package com.lightpro.compta.cmd;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import com.common.utilities.convert.TimeConvert;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceAgeeDesTiersReportParam {
	
	private final String format;
	private final UUID auxiliaryAccountId;
	private final LocalDate startDate;
	private final UUID tiersTypeId;
	
	public BalanceAgeeDesTiersReportParam(){
		throw new UnsupportedOperationException("#BalanceAgeeDesTiersReportParam()");
	}
	
	@JsonCreator
	public BalanceAgeeDesTiersReportParam(@JsonProperty("startDate") final Date startDate,
			@JsonProperty("tiersTypeId") final UUID tiersTypeId,
			@JsonProperty("auxiliaryAccountId") final UUID auxiliaryAccountId,
			@JsonProperty("format") final String format){
				
		this.startDate = TimeConvert.toLocalDate(startDate, ZoneId.systemDefault());
		this.tiersTypeId = tiersTypeId;
		this.auxiliaryAccountId = auxiliaryAccountId;
		this.format = format;
	}
	
	public UUID auxiliaryAccountId(){
		return auxiliaryAccountId;
	}
	
	public UUID tiersTypeId(){
		return tiersTypeId;
	}
	
	public LocalDate startDate(){
		return startDate;
	}
	
	public String format(){
		return format;
	}
}
