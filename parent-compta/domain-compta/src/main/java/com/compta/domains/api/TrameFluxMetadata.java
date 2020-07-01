package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class TrameFluxMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public TrameFluxMetadata() {
		this.domainName = "compta.trame_fluxes";
		this.keyName = "id";
	}
	
	public TrameFluxMetadata(final String domainName, final String keyName){
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
	
	public String descriptionKey(){
		return "description";
	}
	
	public String journalTypeIdKey(){
		return "journal_typeid";
	}
	
	public String orderKey(){
		return "order_prop";
	}
	
	public String isPrincipalKey(){
		return "is_principal";
	}
	
	public String trameIdKey(){
		return "trameid";
	}
	
	public String defaultJournalIdKey(){
		return "default_journalid";
	}	

	public static TrameFluxMetadata create(){
		return new TrameFluxMetadata();
	}	
}
