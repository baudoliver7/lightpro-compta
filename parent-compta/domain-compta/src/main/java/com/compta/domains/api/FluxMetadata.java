package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public final class FluxMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public FluxMetadata() {
		this.domainName = "compta.fluxes";
		this.keyName = "id";
	}
	
	public FluxMetadata(final String domainName, final String keyName){
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
	
	public String objectKey(){
		return "object";
	}
	
	public String journalTypeIdKey(){
		return "journal_typeid";
	}
	
	public String journalIdKey(){
		return "journalid";
	}
	
	public String articleIdKey(){
		return "articleid";
	}
	
	public String orderKey(){
		return "order_prop";
	}
	
	public String isPrincipalKey(){
		return "is_principal";
	}
	
	public static FluxMetadata create(){
		return new FluxMetadata();
	}
}
