package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class TiersAccountMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public TiersAccountMetadata() {
		this.domainName = "compta.tiers_accounts";
		this.keyName = "id";
	}
	
	public TiersAccountMetadata(final String domainName, final String keyName){
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
	
	public String tiersIdKey(){
		return "tiersid";
	}
	
	public String tiersTypeIdKey(){
		return "tiers_typeid";
	}
	
	public String generalAccountIdKey(){
		return "general_accountid";
	}
	
	public static TiersAccountMetadata create(){
		return new TiersAccountMetadata();
	}

}
