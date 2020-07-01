package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class TrameMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public TrameMetadata() {
		this.domainName = "compta.trames";
		this.keyName = "id";
	}
	
	public TrameMetadata(final String domainName, final String keyName){
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
	
	public String pieceTypeIdKey(){
		return "piece_typeid";
	}	

	public static TrameMetadata create(){
		return new TrameMetadata();
	}	
}
