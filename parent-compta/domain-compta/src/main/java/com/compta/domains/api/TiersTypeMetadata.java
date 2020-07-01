package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class TiersTypeMetadata implements DomainMetadata {
	
	private final transient String domainName;
	private final transient String keyName;
	
	public TiersTypeMetadata() {
		this.domainName = "compta.tierstypes";
		this.keyName = "id";
	}
	
	public TiersTypeMetadata(final String domainName, final String keyName){
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
	
	public String nameKey(){
		return "name";
	}
	
	public String codeIdKey(){
		return "codeid";
	}
	
	public String moduleIdKey(){
		return "moduleid";
	}
	
	public String generalAccountIdIdKey(){
		return "general_accountid";
	}
	
	public String sequenceIdKey(){
		return "sequenceid";
	}
	
	public String sequenceLettrageIdKey(){
		return "sequence_lettrage_id";
	}

	public static TiersTypeMetadata create(){
		return new TiersTypeMetadata();
	}	
}
