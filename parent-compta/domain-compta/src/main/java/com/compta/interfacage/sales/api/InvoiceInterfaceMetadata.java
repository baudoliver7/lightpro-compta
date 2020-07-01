package com.compta.interfacage.sales.api;

import com.infrastructure.core.DomainMetadata;

public final class InvoiceInterfaceMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public InvoiceInterfaceMetadata() {
		this.domainName = "compta.sales_facturation_interfaces";
		this.keyName = "id";
	}
	
	public InvoiceInterfaceMetadata(final String domainName, final String keyName){
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
	
	public String journalVenteIdKey(){
		return "journal_venteid";
	}
	
	public String factureClientIdKey(){
		return "facture_clientid";
	}
	
	public String factureDoitTrameIdKey(){
		return "facturedoit_trameid";
	}
	
	public static InvoiceInterfaceMetadata create(){
		return new InvoiceInterfaceMetadata();
	}	
}
