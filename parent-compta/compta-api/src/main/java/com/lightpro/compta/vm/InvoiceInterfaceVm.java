package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.interfacage.sales.api.InvoiceInterface;

public final class InvoiceInterfaceVm {
	
	public final UUID id;
	public final String journalVente;
	public final UUID journalVenteId;
	public final String factureClient;
	public final UUID factureClientId;
	public final String factureDoitTrame;
	public final UUID factureDoitTrameId;
	
	public InvoiceInterfaceVm(){
		throw new UnsupportedOperationException("#FacturationSaleInterfaceVm()");
	}
		
	public InvoiceInterfaceVm(InvoiceInterface origin){
		try {
			this.id = origin.id();
			this.journalVente = origin.journalVente().name();
			this.journalVenteId = origin.journalVente().id();
	        this.factureClient = origin.factureClient().name();
	        this.factureClientId = origin.factureClient().id();
	        this.factureDoitTrame = origin.factureDoitTrame().name();
	        this.factureDoitTrameId = origin.factureDoitTrame().id();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
