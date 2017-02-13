package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class AccountChartMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public AccountChartMetadata() {
		this.domainName = "compta.account_charts";
		this.keyName = "id";
	}
	
	public AccountChartMetadata(final String domainName, final String keyName){
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
	
	public String codeDigitsKey(){
		return "code_digits";
	}
	
	public String moduleIdKey(){
		return "moduleid";
	}
	
	public static AccountChartMetadata create(){
		return new AccountChartMetadata();
	}

}
