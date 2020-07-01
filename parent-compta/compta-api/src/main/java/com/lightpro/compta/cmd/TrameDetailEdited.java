package com.lightpro.compta.cmd;

import java.util.List;
import java.util.UUID;

import com.compta.domains.api.OperationSens;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrameDetailEdited {
	
	private final UUID generalAccountId;
	private final int sensId;
	private final boolean isAggregateAccount;
	private final String formular;
	private final List<TrameDetailSimilaryAccountEdited> similaryAccounts;
	
	public TrameDetailEdited(){
		throw new UnsupportedOperationException("#TrameDetailEdited()");
	}
	
	@JsonCreator
	public TrameDetailEdited( @JsonProperty("generalAccountId") final UUID generalAccountId, 
				    	  @JsonProperty("sensId") final int sensId,
				    	  @JsonProperty("isAggregateAccount") final boolean isAggregateAccount,
				    	  @JsonProperty("formular") final String formular,
				    	  @JsonProperty("similaryAccounts") final List<TrameDetailSimilaryAccountEdited> similaryAccounts){
		
		this.generalAccountId = generalAccountId;
		this.sensId = sensId;
		this.isAggregateAccount = isAggregateAccount;
		this.formular = formular;
		this.similaryAccounts = similaryAccounts;
	}
	
	public UUID generalAccountId(){
		return generalAccountId;
	}
	
	public OperationSens sens(){
		return OperationSens.get(sensId);
	}
	
	public boolean isAggregateAccount(){
		return isAggregateAccount;
	}
	
	public String formular(){
		return formular;
	}
	
	public List<TrameDetailSimilaryAccountEdited> similaryAccounts(){
		return similaryAccounts;
	}
}
