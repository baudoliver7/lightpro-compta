package com.lightpro.compta.cmd;

import java.util.List;
import java.util.UUID;

import com.compta.domains.api.JournalType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class FluxEdited {
	
	private final UUID journalId;
	private final JournalType journalType;
	private final String object;
	private final List<LineEdited> lines;
	
	public FluxEdited(){
		throw new UnsupportedOperationException("#FluxEdited()");
	}
	
	@JsonCreator
	public FluxEdited(  @JsonProperty("journalId") final UUID journalId,
						@JsonProperty("journalTypeId") final int journalTypeId,
						@JsonProperty("object") final String object,
						@JsonProperty("lines") final List<LineEdited> lines){
				
		this.journalId = journalId;
		this.journalType = JournalType.get(journalTypeId);
		this.object = object;
		this.lines = lines;
	}
	
	public UUID journalId(){
		return journalId;
	}
	
	public JournalType journalType(){
		return journalType;
	}
	
	public String object(){
		return object;
	}
	
	public List<LineEdited> lines(){
		return lines;
	}
}
