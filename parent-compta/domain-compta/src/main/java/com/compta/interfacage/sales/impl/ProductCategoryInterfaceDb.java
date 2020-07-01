package com.compta.interfacage.sales.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.impl.TrameNone;
import com.compta.interfacage.sales.api.InvoiceInterface;
import com.compta.interfacage.sales.api.ProductCategoryInterface;
import com.compta.interfacage.sales.api.ProductCategoryInterfaceMetadata;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;

public final class ProductCategoryInterfaceDb extends GuidKeyEntityDb<ProductCategoryInterface, ProductCategoryInterfaceMetadata> implements ProductCategoryInterface {

	private transient final String name;
	private transient final InvoiceInterface invoiceInterface;
	
	public ProductCategoryInterfaceDb(Base base, UUID id, String name, InvoiceInterface invoiceInterface) {
		super(base, id, "Interface des catégories de produits de ventes introuvable !");
		
		this.name = name;
		this.invoiceInterface = invoiceInterface;
	}

	@Override
	public Journal journalVente() throws IOException {
		UUID journalId = ds.get(dm.journalVenteIdKey());
		return invoiceInterface.module().journals().build(journalId);
	}

	@Override
	public Trame factureDoitTrame() throws IOException {
		
		PieceType factureClient = factureClient();
		if(factureClient.isNone())
			return new TrameNone();
			
		UUID trameId = ds.get(dm.factureDoitTrameIdKey());
		return factureClient.trames().build(trameId);
	}

	@Override
	public void update(Journal journalVente, PieceType factureClient, Trame factureDoitTrame) throws IOException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.journalVenteIdKey(), journalVente.id());
		params.put(dm.factureClientIdKey(), factureClient.id());
		
		if(!invoiceInterface.factureClient().isNone()){
			params.put(dm.factureDoitTrameIdKey(), factureDoitTrame.id());
		}else{
			params.put(dm.factureDoitTrameIdKey(), null);
		}		
		
		ds.set(params);
	}

	@Override
	public String name() throws IOException {
		return name;
	}

	@Override
	public PieceType factureClient() throws IOException {
		UUID factureClientId = ds.get(dm.factureClientIdKey());
		return invoiceInterface.module().pieceTypes().build(factureClientId);
	}
}
