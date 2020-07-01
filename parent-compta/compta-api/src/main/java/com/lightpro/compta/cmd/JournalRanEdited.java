package com.lightpro.compta.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JournalRanEdited {
	private final UUID id;
	
	public JournalRanEdited(){
		throw new UnsupportedOperationException("#JournalRanEdited()");
	}
	
	@JsonCreator
	public JournalRanEdited(@JsonProperty("id") final UUID id){		
		this.id = id;
	}
	
	public UUID id(){
		return id;
	}
}
