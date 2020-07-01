package com.lightpro.compta.cmd;

import java.util.List;
import java.util.UUID;

import com.compta.domains.api.JournalType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrameFluxEdited {
	
	private final List<TrameDetailEdited> details;
	private final String description;
	private final JournalType journalType;
	private final UUID defaultJournalId;
	
	public TrameFluxEdited(){
		throw new UnsupportedOperationException("#TrameFluxEdited()");
	}
	
	@JsonCreator
	public TrameFluxEdited(@JsonProperty("description") final String description,
						   @JsonProperty("journalTypeId") final int journalTypeId,
						   @JsonProperty("defaultJournalId") final UUID defaultJournalId,
			               @JsonProperty("details") final List<TrameDetailEdited> details){
		
		this.description = description;
		this.journalType = JournalType.get(journalTypeId);
		this.defaultJournalId = defaultJournalId;
		this.details = details;
	}
	
	public String description(){
		return description;
	}
	
	public JournalType journalType(){
		return journalType;
	}
	
	public UUID defaultJournalId(){
		return defaultJournalId;
	}
	
	public List<TrameDetailEdited> details(){
		return details;
	}
}
