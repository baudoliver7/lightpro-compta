package com.compta.interfacage.sales.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.impl.JournalNone;
import com.compta.domains.impl.PieceTypeNone;
import com.compta.domains.impl.TrameNone;
import com.compta.interfacage.sales.api.ProductCategoryInterface;
import com.compta.interfacage.sales.api.ProductCategoryInterfaceMetadata;
import com.compta.interfacage.sales.api.ProductCategoryInterfaces;
import com.infrastructure.core.GuidKeyQueryableDb;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;

public class ProductCategoryInterfacesDb extends GuidKeyQueryableDb<ProductCategoryInterface, ProductCategoryInterfaceMetadata> implements ProductCategoryInterfaces {

	private final Compta compta;
	
	public ProductCategoryInterfacesDb(Base base, Compta compta){
		super(base, "Interface des catégories de produits de ventes introuvable !");
		this.compta = compta;
	}
	
	private ProductCategoryInterface add(UUID id, Journal journalVente, PieceType factureClient, Trame factureDoitTrame) throws IOException{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.journalVenteIdKey(), journalVente.id());
		params.put(dm.factureClientIdKey(), factureClient.id());	
		params.put(dm.factureDoitTrameIdKey(), factureDoitTrame.id());
		
		DomainsStore ds = base.domainsStore(dm);
		ds.set(id, params);
		
		return get(id);
	}

	@Override
	public List<ProductCategoryInterface> all() throws IOException {
		
		if(!compta.interfacage().salesInterface().available())
			return new ArrayList<ProductCategoryInterface>();
		
		List<Object> results = base.executeQuery(String.format("SELECT id, name FROM sales.product_categories WHERE %s=?", HorodateMetadata.create().ownerCompanyIdKey()), Arrays.asList(compta.company().id()));
		
		List<ProductCategoryInterface> items = new ArrayList<ProductCategoryInterface>();
		for (int i = 0; i < results.size(); i+=2) {
			
			UUID id = UUIDConvert.fromObject(results.get(i));
			String name = results.get(i+1).toString();
			
			ProductCategoryInterface productInterface;
			if(!ds.exists(id))
				productInterface = add(id, new JournalNone(), new PieceTypeNone(), new TrameNone());
			else
				productInterface = new ProductCategoryInterfaceDb(base, id, name, compta.interfacage().salesInterface().invoiceInterface());
			
			items.add(productInterface);
		}
		
		return items;
	}

	@Override
	public ProductCategoryInterface get(UUID id) throws IOException {
		ProductCategoryInterface productInterface = null;
		
		for (ProductCategoryInterface item : all()) {
			if(item.id().equals(id))
				productInterface = item;
		}
		
		if(productInterface == null)
			throw new IllegalArgumentException(msgNotFound);
		
		return productInterface;
	}

	@Override
	protected ProductCategoryInterface newOne(UUID id) {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public ProductCategoryInterface none() {
		throw new UnsupportedOperationException("Opération non supportée !"); 
	}
}
