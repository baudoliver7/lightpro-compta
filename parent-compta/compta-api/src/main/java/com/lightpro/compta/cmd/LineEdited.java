package com.lightpro.compta.cmd;

import java.sql.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LineEdited {
	
	private final UUID generalAccountId;
	private final double debit;
	private final double credit;
	
	public LineEdited(){
		throw new UnsupportedOperationException("#LineEdited()");
	}
	
	@JsonCreator
	public LineEdited(  @JsonProperty("dateEcheance") final Date dateEcheance,
						@JsonProperty("generalAccountId") final UUID generalAccountId, 
				    	@JsonProperty("debit") final double debit,
				    	@JsonProperty("credit") final double credit){
		
		this.generalAccountId = generalAccountId;		
		this.debit = debit;
		this.credit = credit;
	}
	
	public UUID generalAccountId(){
		return generalAccountId;
	}
	
	public double debit(){
		return debit;
	}
	
	public double credit(){
		return credit;
	}
}
