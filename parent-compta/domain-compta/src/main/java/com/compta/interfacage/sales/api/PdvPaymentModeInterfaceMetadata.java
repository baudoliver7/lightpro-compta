package com.compta.interfacage.sales.api;

import com.infrastructure.core.DomainMetadata;

public final class PdvPaymentModeInterfaceMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public PdvPaymentModeInterfaceMetadata() {
		this.domainName = "compta.sales_pdv_payment_mode_interfaces";
		this.keyName = "id";
	}
	
	public PdvPaymentModeInterfaceMetadata(final String domainName, final String keyName){
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
	
	public String journalEncaissementIdKey(){
		return "journal_encaissementid";
	}
	
	public String reglementIdKey(){
		return "reglementid";
	}
	
	public String acompteTrameIdKey(){
		return "acompte_trameid";
	}
	
	public String provisionTrameIdKey(){
		return "provision_trameid";
	}
	
	public String reglementDefinitifTrameIdKey(){
		return "reglement_definitif_trameid";
	}
	
	public String paymentModeIdKey(){
		return "payment_modeid";
	}
	
	public String modulePdvIdKey(){
		return "module_pdvid";
	}
	
	public static PdvPaymentModeInterfaceMetadata create(){
		return new PdvPaymentModeInterfaceMetadata();
	}	
}
