package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class PieceTypeMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public PieceTypeMetadata() {
		this.domainName = "compta.piece_types";
		this.keyName = "id";
	}
	
	public PieceTypeMetadata(final String domainName, final String keyName){
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
	
	public String usageCodeIdKey(){
		return "usagecodeid";
	}
	
	public String tiersTypeManagedIdKey(){
		return "tierstype_managedid";
	}
	
	public String natureIdKey(){
		return "natureid";
	}
	
	public String sequenceIdKey(){
		return "sequenceid";
	}
	
	public String objectKey(){
		return "object";
	}
	
	public String moduleIdKey(){
		return "moduleid";
	}

	public String preferredTrameIdKey(){
		return "preferred_trameid";
	}
	
	public static PieceTypeMetadata create(){
		return new PieceTypeMetadata();
	}	
}
