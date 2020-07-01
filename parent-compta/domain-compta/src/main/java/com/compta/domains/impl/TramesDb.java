package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Compta;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.api.TrameMetadata;
import com.compta.domains.api.Trames;
import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;

public final class TramesDb extends GuidKeyAdvancedQueryableDb<Trame, TrameMetadata> implements Trames {

	private transient final PieceType pieceType;
	private final Compta module;
	
	public TramesDb(Base base, PieceType pieceType, Compta module) {
		super(base, "Trame introuvable !");
		
		this.pieceType = pieceType;
		this.module = module;
	}

	@Override
	public Trame add(String name) throws IOException {
		
		if(StringUtils.isBlank(name))
			throw new IllegalArgumentException("Vous devez renseigner le libellé du modèle de saisie !");
		
		if(pieceType.isNone())
			throw new IllegalArgumentException("Vous devez spécifier le type de pièce !");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);	
		params.put(dm.pieceTypeIdKey(), pieceType.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter); 
		
		String statement = String.format("%s tr "
				+ "WHERE tr.%s=?",				 
				dm.domainName(), 
				dm.pieceTypeIdKey());
		
		params.add(pieceType.id());
		
		String orderClause = String.format("ORDER BY tr.%s DESC", dm.nameKey());
				
		String keyResult = String.format("tr.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected Trame newOne(UUID id) {
		return new TrameDb(base, id, module);
	}

	@Override
	public Trame none() {
		return new TrameNone();
	}

	@Override
	public void delete(Trame item) throws IOException {
		item.fluxes().deleteAll();
		super.delete(item);
	}
	
	@Override
	public void deleteAll() throws IOException {
		for (Trame item : all()) {
			delete(item);
		}
	}
}
