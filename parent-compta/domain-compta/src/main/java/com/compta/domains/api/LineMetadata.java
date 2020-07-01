package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class LineMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public LineMetadata() {
		this.domainName = "compta.lines";
		this.keyName = "id";
	}
	
	public LineMetadata(final String domainName, final String keyName){
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
	
	public String generalAccountIdKey(){
		return "general_accountid";
	}
	
	public String auxiliaryAccountIdKey(){
		return "auxiliary_accountid";
	}
	
	public String debitKey(){
		return "debit";
	}
	
	public String creditKey(){
		return "credit";
	}
	
	public String fluxIdKey(){
		return "fluxid";
	}
	
	public String orderKey(){
		return "order_lp";
	}

	public String lettrageKey(){
		return "lettrage";
	}
	
	public String reconcileIdKey(){
		return "reconcileid";
	}
	
	public static LineMetadata create(){
		return new LineMetadata();
	}	
}
