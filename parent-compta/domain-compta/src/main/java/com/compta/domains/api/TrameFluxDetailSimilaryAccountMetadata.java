package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class TrameFluxDetailSimilaryAccountMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public TrameFluxDetailSimilaryAccountMetadata() {
		this.domainName = "compta.trame_flux_detail_similary_accounts";
		this.keyName = "id";
	}
	
	public TrameFluxDetailSimilaryAccountMetadata(final String domainName, final String keyName){
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
	
	public String accountIdKey(){
		return "accountid";
	}
	
	public String detailIdKey(){
		return "detailid";
	}	
	
	public String orderKey(){
		return "order_prop";
	}

	public static TrameFluxDetailSimilaryAccountMetadata create(){
		return new TrameFluxDetailSimilaryAccountMetadata();
	}	
}
