package com.lightpro.compta.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class TrameDetailSimilaryAccountEdited {
	
	private final UUID id;
	
	public TrameDetailSimilaryAccountEdited(){
		throw new UnsupportedOperationException("#TrameDetailSimilaryAccountEdited()");
	}
	
	@JsonCreator
	public TrameDetailSimilaryAccountEdited(@JsonProperty("id") final UUID id){		
		this.id = id;
	}
	
	public UUID id(){
		return id;
	}
}
