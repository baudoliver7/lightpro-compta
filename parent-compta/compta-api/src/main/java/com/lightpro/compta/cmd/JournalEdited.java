package com.lightpro.compta.cmd;

import java.util.UUID;

import com.compta.domains.api.JournalType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JournalEdited {
	private final String name;
	private final String code;
	private final JournalType type;
	private final UUID accountId;
	private final boolean isViewedOnDashboard;
	private final boolean validateAccount;
	
	public JournalEdited(){
		throw new UnsupportedOperationException("#JournalEdited()");
	}
	
	@JsonCreator
	public JournalEdited( @JsonProperty("name") final String name, 
				    	  @JsonProperty("code") final String code,
				    	  @JsonProperty("typeId") final int typeId,
				    	  @JsonProperty("accountId") final UUID accountId,
				    	  @JsonProperty("isViewedOnDashboard") final boolean isViewedOnDashboard,
				    	  @JsonProperty("validateAccount") final boolean validateAccount){
		
		this.name = name;
		this.code = code;
		this.type = JournalType.get(typeId);
		this.accountId = accountId;
		this.isViewedOnDashboard = isViewedOnDashboard;
		this.validateAccount = validateAccount;
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
	
	public UUID accountId(){
		return accountId;
	}
	
	public boolean isViewedOnDashboard(){
		return isViewedOnDashboard;
	}
	
	public boolean validateAccount(){
		return validateAccount;
	}
}
