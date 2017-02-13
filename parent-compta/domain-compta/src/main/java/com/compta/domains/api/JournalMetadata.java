package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class JournalMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public JournalMetadata() {
		this.domainName = "compta.journals";
		this.keyName = "id";
	}
	
	public JournalMetadata(final String domainName, final String keyName){
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
	
	public String moduleIdKey(){
		return "moduleid";
	}
	
	public String typeIdKey(){
		return "typeid";
	}

	public static JournalMetadata create(){
		return new JournalMetadata();
	}	
}
