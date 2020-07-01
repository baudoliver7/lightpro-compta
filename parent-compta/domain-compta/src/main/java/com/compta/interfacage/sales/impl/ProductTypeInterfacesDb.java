package com.compta.interfacage.sales.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.interfacage.sales.api.ProductTypeInterface;
import com.compta.interfacage.sales.api.ProductTypeInterfaceMetadata;
import com.compta.interfacage.sales.api.ProductTypeInterfaces;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;

public final class ProductTypeInterfacesDb implements ProductTypeInterfaces {

	private final Base base;
	private final Compta compta;
	private final ProductTypeInterfaceMetadata dm;
	private final DomainsStore ds;
	
	public ProductTypeInterfacesDb(final Base base, final Compta compta){
		this.base = base;
		this.compta = compta;
		this.dm = ProductTypeInterfaceMetadata.create();
		this.ds = base.domainsStore(dm);
	}
	
	@Override
	public List<ProductTypeInterface> all() throws IOException {
		
		List<ProductTypeInterface> items = new ArrayList<ProductTypeInterface>();
		
		String statement = String.format("SELECT %s FROM %s", dm.keyName(), dm.domainName());
		List<Object> results = base.executeQuery(statement, Arrays.asList());
		
		UUID companyId = compta.company().id();
		for (Object id : results) {
			List<Object> keys = base.executeQuery(String.format("SELECT %s FROM %s WHERE %s=? AND %s=?", dm.keyName(), dm.domainName(), dm.typeIdIdKey(), HorodateMetadata.create().ownerCompanyIdKey()), Arrays.asList(id, companyId));
			
			if(keys.isEmpty())
			{
				// créer l'interface
				Map<String, Object> params = new HashMap<>();
				params.put(dm.typeIdIdKey(), id);
				
				ds.set(UUID.randomUUID(), params);				
			}
			
			items.add(new ProductTypeInterfaceDb(base, Integer.parseInt(id.toString()), compta));  
		}
		
		return items;
	}

	@Override
	public ProductTypeInterface get(int id) throws IOException {
		
		for (ProductTypeInterface item : all()) {
			if(item.id() == id)
				return item;
		}
		
		throw new IllegalArgumentException("Interface de type de produit introuvable !");
	}

}
