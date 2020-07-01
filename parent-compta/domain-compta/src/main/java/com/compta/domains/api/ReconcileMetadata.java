package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class ReconcileMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public ReconcileMetadata() {
		this.domainName = "compta.reconciles";
		this.keyName = "id";
	}
	
	public ReconcileMetadata(final String domainName, final String keyName){
		this.domainName = domainName;
		this.keyName = keyName;
	}
	
	@Override
	public String domainName() {
		return this.domainName;
	}

	@Override
	public String keyName() {
		return this.keyName;
	}
	
	public String debitKey(){
		return "debit";
	}
	
	public String creditKey(){
		return "credit";
	}
	
	public String lettratedKey(){
		return "lettrated";
	}
	
	public String auxiliaryAccountIdKey(){
		return "auxiliary_accountid";
	}
	
	public String moduleIdKey(){
		return "moduleid";
	}

	public static ReconcileMetadata create(){
		return new ReconcileMetadata();
	}	
}
