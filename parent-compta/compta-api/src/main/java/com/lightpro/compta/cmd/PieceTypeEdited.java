package com.lightpro.compta.cmd;

import java.util.UUID;

import com.compta.domains.api.PieceNature;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PieceTypeEdited {
	
	private final String name;
	private final UUID tiersTypeManagedId;
	private final PieceNature nature;
	private final String object;
	private final UUID sequenceId;	
	private final UUID preferredTrameId;
	
	public PieceTypeEdited(){
		throw new UnsupportedOperationException("#PieceTypeEdited()");
	}
	
	@JsonCreator
	public PieceTypeEdited( @JsonProperty("name") final String name, 
				    	  	@JsonProperty("tiersTypeManagedId") final UUID tiersTypeManagedId,
				    	  	@JsonProperty("natureId") final int natureId,
				    	  	@JsonProperty("sequenceId") final UUID sequenceId,
				    	  	@JsonProperty("preferredTrameId") final UUID preferredTrameId,
				    	  	@JsonProperty("object") final String object){
		
		this.name = name;
		this.tiersTypeManagedId = tiersTypeManagedId;
		this.nature = PieceNature.get(natureId);
		this.object = object;
		this.sequenceId = sequenceId;
		this.preferredTrameId = preferredTrameId;
	}
	
	public String name(){
		return name;
	}
	
	public UUID tiersTypeManagedId(){
		return tiersTypeManagedId;
	}
	
	public PieceNature nature(){
		return nature;
	}
	
	public UUID sequenceId(){
		return sequenceId;
	}
	
	public UUID preferredTrameId(){
		return preferredTrameId;
	}
	
	public String object(){
		return object;
	}
}
