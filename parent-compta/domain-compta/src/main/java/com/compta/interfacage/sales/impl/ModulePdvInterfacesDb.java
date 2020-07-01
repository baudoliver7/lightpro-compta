package com.compta.interfacage.sales.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Compta;
import com.compta.interfacage.sales.api.ModulePdvInterface;
import com.compta.interfacage.sales.api.ModulePdvInterfaceMetadata;
import com.compta.interfacage.sales.api.ModulePdvInterfaces;
import com.infrastructure.core.GuidKeyQueryableDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;

public final class ModulePdvInterfacesDb extends GuidKeyQueryableDb<ModulePdvInterface, ModulePdvInterfaceMetadata> implements ModulePdvInterfaces {

	private final transient Compta module;
	
	public ModulePdvInterfacesDb(final Base base, final Compta module){
		super(base, "Module de vente introuvable !");
		this.module = module;
	}
	
	private QueryBuilder buildQuery(String filter) throws IOException {
		
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		String statement = String.format("%s mpd "
				+ "WHERE mpd.%s=? AND mpd.%s ILIKE ?",				 
				dm.domainName(), 
				dm.companyIdKey(), dm.nameKey());
		
		params.add(module.company().id());
		params.add("%" + filter + "%"); 
		

		String orderClause = String.format("ORDER BY mpd.%s ASC", dm.nameKey());
		
		String keyResult = String.format("mpd.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}
	
	@Override
	public List<ModulePdvInterface> all() throws IOException {
		
		return  buildQuery(StringUtils.EMPTY).find()
				  .stream()
				  .map(m -> build(UUIDConvert.fromObject(m)))
				  .collect(Collectors.toList());
	}

	@Override
	protected ModulePdvInterface newOne(UUID id) {
		return new ModulePdvInterfaceDb(base, id, module);
	}

	@Override
	public ModulePdvInterface none() {
		return new ModulePdvInterfaceNone();
	}
}
