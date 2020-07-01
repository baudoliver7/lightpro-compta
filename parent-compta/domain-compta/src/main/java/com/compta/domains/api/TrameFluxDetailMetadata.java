package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class TrameFluxDetailMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public TrameFluxDetailMetadata() {
		this.domainName = "compta.trame_flux_details";
		this.keyName = "id";
	}
	
	public TrameFluxDetailMetadata(final String domainName, final String keyName){
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
	
	public String orderKey(){
		return "order_prop";
	}
	
	public String sensIdKey(){
		return "sensid";
	}
	
	public String trameFluxIdKey(){
		return "trame_fluxid";
	}
	
	public String isAggregateAccountKey(){
		return "is_aggregate_account";
	}
	
	public String formular(){
		return "formular";
	}	

	public static TrameFluxDetailMetadata create(){
		return new TrameFluxDetailMetadata();
	}	
}
