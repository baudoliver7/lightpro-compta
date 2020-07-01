package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.PieceArticle;
import com.compta.domains.api.Trame;
import com.compta.domains.api.TrameFluxes;
import com.compta.domains.api.TrameMetadata;
import com.compta.domains.api.TrameFlux;
import com.compta.domains.api.TrameFluxMetadata;
import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;

public final class TrameFluxesDb extends GuidKeyAdvancedQueryableDb<TrameFlux, TrameFluxMetadata> implements TrameFluxes {

	private transient final Trame trame;
	private final Compta module;
	
	public TrameFluxesDb(final Base base, final Trame trame, final Compta module) {
		super(base, "Flux de trame introuvable !");
		
		this.trame = trame;
		this.module = module;
	}

	@Override
	public TrameFlux add(String description, JournalType journalType, Journal defaultJournal) throws IOException {
				
		if(trame.isNone())
			throw new IllegalArgumentException("Vous devez spécifier le modèle de saisie de cette écriture !");		
		
		int order = all().size() + 1;
		
		if(order > 1)
		{
			if(journalType == JournalType.NONE)			
				throw new IllegalArgumentException(String.format("Vous devez sélectionner un type de journal pour chaque flux sécondaire !"));
			
			if(defaultJournal.isNone())
				throw new IllegalArgumentException("Vous devez spécifier un journal par défaut !");					
		}
		
		Map<String, Object> params = new HashMap<String, Object>();	
		params.put(dm.descriptionKey(), description);
		params.put(dm.journalTypeIdKey(), journalType.id());
		params.put(dm.defaultJournalIdKey(), defaultJournal.id());
		params.put(dm.trameIdKey(), trame.id());				
		params.put(dm.isPrincipalKey(), order == 1);		
		params.put(dm.orderKey(), order);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	public void delete(TrameFlux item) throws IOException {
		item.details().deleteAll();
		super.delete(item);
	}
	
	@Override
	public void deleteAll() throws IOException {
		for (TrameFlux item : all()) {
			delete(item);
		}
	}

	@Override
	public void validate() throws IOException {
		for (TrameFlux item : all()) {
			item.validate();
		}		
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		TrameMetadata tmDm = TrameMetadata.create();
		String statement = String.format("%s trf "
				+ "JOIN %s tp ON tp.%s = trf.%s "
				+ "WHERE trf.%s=?",				 
				dm.domainName(), 
				tmDm.domainName(), tmDm.keyName(), dm.trameIdKey(),
				dm.trameIdKey());
		
		params.add(trame.id());
		
		String orderClause = String.format("ORDER BY trf.%s ASC", dm.orderKey());
		
		String keyResult = String.format("trf.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected TrameFlux newOne(UUID id) {
		return new TrameFluxDb(base, id, module);
	}

	@Override
	public TrameFlux none() {
		return new TrameFluxNone();
	}

	@Override
	public PieceArticle generate(Journal journal, Map<String, Double> params) throws IOException {
		return new PieceArticleTemplate(this, journal, params);
	}

	@Override
	public PieceArticle generateReverse(Journal journal, Map<String, Double> params) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}	
}
