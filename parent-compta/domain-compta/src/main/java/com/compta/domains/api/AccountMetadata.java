package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class AccountMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public AccountMetadata() {
		this.domainName = "compta.accounts";
		this.keyName = "id";
	}
	
	public AccountMetadata(final String domainName, final String keyName){
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
	
	public String codeKey(){
		return "code";
	}
	
	public String chartIdKey(){
		return "chartid";
	}
	
	public String typeIdKey(){
		return "typeid";
	}

	public static AccountMetadata create(){
		return new AccountMetadata();
	}	
}
