package com.lightpro.compta.cmd;

import com.compta.domains.api.AccountType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountEdited {
	
	private final String name;
	private final String code;
	private final AccountType type;
	
	public AccountEdited(){
		throw new UnsupportedOperationException("#AccountEdited()");
	}
	
	@JsonCreator
	public AccountEdited( @JsonProperty("name") final String name, 
				    	  @JsonProperty("code") final String code,
				    	  @JsonProperty("typeId") final int typeId){
		
		this.name = name;
		this.code = code;
		this.type = AccountType.get(typeId);
	}
	
	public String name(){
		return name;
	}
	
	public String code(){
		return code;
	}
	
	public AccountType type(){
		return type;
	}
}
