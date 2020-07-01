package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.TiersCode;
import com.compta.domains.api.TiersType;
import com.compta.domains.api.TiersTypeMetadata;
import com.compta.domains.api.TiersTypes;
import com.infrastructure.core.GuidKeyQueryableDb;
import com.infrastructure.datasource.Base;
import com.securities.api.Sequence;

public final class TiersTypesDb extends GuidKeyQueryableDb<TiersType, TiersTypeMetadata> implements TiersTypes {

	private final transient Compta module;
	
	public TiersTypesDb(final Base base, final Compta module){
		super(base, "Type de tiers introuvable !");
		this.module = module;
	}
	
	@Override
	public List<TiersType> all() throws IOException {
		
		String statement = String.format("SELECT %s FROM %s "
				+ "WHERE %s=? "
				+ "ORDER BY %s ASC",
				dm.keyName(), dm.domainName(), 
				dm.moduleIdKey(),
				dm.nameKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(module.id());
		
		return  ds.find(statement, params)
				  .stream()
				  .map(m -> build(UUIDConvert.fromObject(m)))
				  .collect(Collectors.toList());
	}

	@Override
	public TiersType add(String name, TiersCode code, Account generalAccount, Sequence sequence, Sequence sequenceLettrage) throws IOException {
		TiersTypeDb.validate(name, generalAccount, sequence, sequenceLettrage);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);	
		params.put(dm.codeIdKey(), code.id());	
		params.put(dm.generalAccountIdIdKey(), generalAccount.id());
		params.put(dm.moduleIdKey(), module.id());
		params.put(dm.sequenceIdKey(), sequence.id());	
		params.put(dm.sequenceLettrageIdKey(), sequenceLettrage.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);		
	}

	@Override
	public TiersType add(String name, Account generalAccount, Sequence sequence, Sequence sequenceLettrage) throws IOException {
		return add(name, TiersCode.OTHER, generalAccount, sequence, sequenceLettrage);
	}

	@Override
	public void delete(TiersType item) throws IOException {
		if(contains(item))
		{
			Sequence sequence = item.sequence();
			Sequence sequenceLettrage = item.sequenceLettrage();
			ds.delete(item.id());
			
			if(!sequence.isNone())
				module.sequences().delete(sequence);
			
			if(!sequenceLettrage.isNone())
				module.sequences().delete(sequenceLettrage);
		}
	}

	@Override
	public TiersType none() {
		return new TiersTypeNone();
	}

	@Override
	protected TiersType newOne(UUID id) {
		return new TiersTypeDb(base, id, module);
	}
}
