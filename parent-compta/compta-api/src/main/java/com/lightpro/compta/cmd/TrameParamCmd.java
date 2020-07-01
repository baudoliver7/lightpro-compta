package com.lightpro.compta.cmd;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrameParamCmd {
	
	private final UUID trameId;
	private final UUID journalId;
	private final List<TrameParamDetailCmd> params;
	
	public TrameParamCmd(){
		throw new UnsupportedOperationException("#TrameParamCmd()");
	}
	
	@JsonCreator
	public TrameParamCmd(@JsonProperty("trameId") final UUID trameId,
			@JsonProperty("journalId") final UUID journalId,
			@JsonProperty("params") final List<TrameParamDetailCmd> params){	
		
		this.trameId = trameId;
		this.journalId = journalId;
		this.params = params;		
	}
	
	public UUID trameId(){
		return trameId;
	}
	
	public UUID journalId(){
		return journalId;
	}
	public List<TrameParamDetailCmd> params(){
		return params;
	}
}
