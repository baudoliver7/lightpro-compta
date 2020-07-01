package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.interfacage.sales.api.ProductCategoryInterface;

public final class ProductCategoryInterfaceVm {
	
	public final UUID id;
	public final String name;
	public final String journalVente;
	public final UUID journalVenteId;
	public final String factureClient;
	public final UUID factureClientId;
	
	public ProductCategoryInterfaceVm(){
		throw new UnsupportedOperationException("#ProductCategorySaleInterfaceVm()");
	}
		
	public ProductCategoryInterfaceVm(ProductCategoryInterface origin){
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.journalVente = origin.journalVente().name();
	        this.journalVenteId = origin.journalVente().id();
	        this.factureClient = origin.factureClient().name();
	        this.factureClientId = origin.factureClient().id();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
}
