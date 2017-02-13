package com.lightpro.compta.cmd;

import com.compta.domains.api.JournalType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JournalEdited {
	private final String name;
	private final String code;
	private final JournalType type;
	
	public JournalEdited(){
		throw new UnsupportedOperationException("#JournalEdited()");
	}
	
	@JsonCreator
	public JournalEdited( @JsonProperty("name") final String name, 
				    	  @JsonProperty("code") final String code,
				    	  @JsonProperty("typeId") final int typeId){
		
		this.name = name;
		this.code = code;
		this.type = JournalType.get(typeId);
	}
	
	public String name(){
		return name;
	}
	
	public String code(){
		return code;
	}
	
	public JournalType type(){
		return type;
	}
}
