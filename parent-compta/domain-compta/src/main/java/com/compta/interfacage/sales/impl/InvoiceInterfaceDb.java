package com.compta.interfacage.sales.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.impl.TrameNone;
import com.compta.interfacage.sales.api.InvoiceInterface;
import com.compta.interfacage.sales.api.InvoiceInterfaceMetadata;
import com.compta.interfacage.sales.api.ProductCategoryInterfaces;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;

public final class InvoiceInterfaceDb extends GuidKeyEntityDb<InvoiceInterface, InvoiceInterfaceMetadata> implements InvoiceInterface {

	private final Compta module;
	
	public InvoiceInterfaceDb(Base base, UUID id, Compta module) {
		super(base, id, "Interface de facturation des ventes introuvable !");
		this.module = module;
	}
	
	@Override
	public Journal journalVente() throws IOException {
		UUID journalId = ds.get(dm.journalVenteIdKey());
		return module().journals().build(journalId);
	}

	@Override
	public PieceType factureClient() throws IOException {
		UUID pieceTypeId = ds.get(dm.factureClientIdKey());
		return module().pieceTypes().build(pieceTypeId);
	}

	@Override
	public Trame factureDoitTrame() throws IOException {
		if(factureClient().isNone())
			return new TrameNone();
		
		UUID trameId = ds.get(dm.factureDoitTrameIdKey());
		return factureClient().trames().build(trameId);		
	}

	@Override
	public ProductCategoryInterfaces productCategories() throws IOException {
		return new ProductCategoryInterfacesDb(base, module);
	}

	@Override
	public void update(Journal journalVente, PieceType factureClient, Trame factureDoitTrame) throws IOException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.journalVenteIdKey(), journalVente.id());	
		params.put(dm.factureClientIdKey(), factureClient.id());
		
		if(!factureClient.isNone()){
			params.put(dm.factureDoitTrameIdKey(), factureDoitTrame.id());
		}else{
			params.put(dm.factureDoitTrameIdKey(), null);
		}		
		
		ds.set(params);
	}

	@Override
	public Compta module() throws IOException {
		return module;
	}
}
