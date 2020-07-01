package com.lightpro.compta.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TiersTypeEdited {

	private final UUID generalAccountId;
	private final String name;
	private final UUID sequenceId;
	private final UUID sequenceLettrageId;
	
	public TiersTypeEdited(){
		throw new UnsupportedOperationException("#TiersTypeEdited()");
	}
	
	@JsonCreator
	public TiersTypeEdited( @JsonProperty("generalAccountId") final UUID generalAccountId, 
				    	  	@JsonProperty("name") final String name,
				    	  	@JsonProperty("sequenceId") final UUID sequenceId,
				    	  	@JsonProperty("sequenceLettrageId") final UUID sequenceLettrageId){
		
		this.generalAccountId = generalAccountId;
		this.name = name;
		this.sequenceId = sequenceId;		
		this.sequenceLettrageId = sequenceLettrageId;
	}
	
	public String name(){
		return name;
	}
	
	public UUID generalAccountId(){
		return generalAccountId;
	}
	
	public UUID sequenceId(){
		return sequenceId;
	}
	
	public UUID sequenceLettrageId(){
		return sequenceLettrageId;
	}
}
