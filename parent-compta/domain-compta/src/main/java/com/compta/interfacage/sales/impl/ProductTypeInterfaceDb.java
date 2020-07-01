package com.compta.interfacage.sales.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.interfacage.sales.api.ProductTypeInterface;
import com.compta.interfacage.sales.api.ProductTypeInterfaceMetadata;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;

public final class ProductTypeInterfaceDb implements ProductTypeInterface {

	private final Base base;
	private final int id;
	private final Compta module;
	private final ProductTypeInterfaceMetadata dm;
	private final DomainStore ds;
	private final UUID internalId;
	
	public ProductTypeInterfaceDb(final Base base, final int id, final Compta module){
		this.base = base;
		this.id = id;
		this.module = module;
		this.dm = ProductTypeInterfaceMetadata.create();
		
		this.internalId = internalId();
		
		if(internalId() == null)
			throw new IllegalArgumentException("Interface de type de produit introuvable !");
		
		this.ds = base.domainsStore(dm).createDs(internalId);		
	}
	
	private UUID internalId(){
		
		String statement = String.format("SELECT %s FROM %s WHERE %s=? AND %s=?", dm.keyName(), dm.domainName(), dm.typeIdIdKey(), HorodateMetadata.create().ownerCompanyIdKey());
		List<Object> results;
		
		try {
			results = base.executeQuery(statement, Arrays.asList(id, module.company().id()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		if(results.isEmpty())
			return null;
		
		return UUIDConvert.fromObject(results.get(0));
	}
	
	@Override
	public int id() {
		return id;
	}

	@Override
	public String name() throws IOException {
		String statement = String.format("SELECT pct.name FROM sales.product_category_types pct "
										+ "JOIN %s pcti ON pcti.%s=pct.id "
										+ "WHERE %s=?",
										dm.domainName(), dm.typeIdIdKey(), 
										dm.keyName());
		
		return base.executeQuery(statement, Arrays.asList(internalId)).get(0).toString();
	}

	@Override
	public Account account() throws IOException {
		UUID accountId = ds.get(dm.accountIdKey());
		return module.accounts().build(accountId);
	}
}
