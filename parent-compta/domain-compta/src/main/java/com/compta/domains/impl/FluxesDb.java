package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Flux;
import com.compta.domains.api.FluxMetadata;
import com.compta.domains.api.Fluxes;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.PieceArticle;
import com.compta.domains.api.PieceStatus;
import com.infrastructure.core.GuidKeyQueryableDb;
import com.infrastructure.datasource.Base;

public final class FluxesDb extends GuidKeyQueryableDb<Flux, FluxMetadata> implements Fluxes {

	private final transient PieceArticle article;
	private final Compta module;
	
	public FluxesDb(Base base, PieceArticle article, Compta module) {
		super(base, "Flux introuvable !");
		
		this.article = article;
		this.module = module;
	}

	@Override
	public List<Flux> all() throws IOException {
		List<Object> params = new ArrayList<Object>();

		String statement = String.format("%s fl "
				+ "WHERE fl.%s=?",				 
				dm.domainName(), 
				dm.articleIdKey());
		
		params.add(article.id());
		
		String orderClause = String.format("ORDER BY fl.%s ASC", dm.orderKey());
		
		String keyResult = String.format("fl.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause)
				   .find()
				   .stream()
				   .map(m -> new FluxDb(base, UUIDConvert.fromObject(m), module))
				   .collect(Collectors.toList());
	}

	@Override
	public boolean contains(Flux item) {
		try {
			return !item.isNone() 
					&& item.article().equals(article);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Flux newOne(UUID id) {
		return new FluxDb(base, id, module);
	}

	@Override
	public Flux none() {
		return new FluxNone();
	}

	@Override
	public Flux Principal() throws IOException {
		return all().get(0);
	}

	@Override
	public void delete(Flux item) throws IOException {
		if(article.piece().status() == PieceStatus.ACCOUNTED)
			throw new IllegalArgumentException("Vous ne pouvez mettre à jour les écritures d'une pièce comptabilisée !");
		
		if(contains(item)){
			item.lines().deleteAll();
			ds.delete(item.id());
		}
	}
	
	@Override
	public void deleteAll() throws IOException {
		for (Flux item : all()) {
			delete(item);
		}
	}

	@Override
	public Flux add(String object, JournalType journalType, Journal journal) throws IOException {
		
		if(journal.isNone())
			throw new IllegalArgumentException("Vous devez spécifier le journal dans lequel passer l'écriture !");
		
		if(article.piece().status() == PieceStatus.ACCOUNTED)
			throw new IllegalArgumentException("Vous ne pouvez mettre à jour les écritures d'une pièce comptabilisée !");
		
		if(StringUtils.isBlank(object))
			throw new IllegalArgumentException("Vous devez spécifier un libellé pour l'opération !");
		
		int order = all().size() + 1;
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(order == 1){
			params.put(dm.journalTypeIdKey(), journal.type().id());
			params.put(dm.journalIdKey(), journal.id());
		}else{
			if(journalType == JournalType.NONE)
				throw new IllegalArgumentException("Vous devez spécifier un type de journal !");
			
			if(journal.isNone())
				throw new IllegalArgumentException("Vous devez spécifier un journal !");
			
			if(journal.type() != journalType)
				throw new IllegalArgumentException("Le journal n'est pas compatible au type spécifié !");
			
			params.put(dm.journalTypeIdKey(), journalType.id());
			params.put(dm.journalIdKey(), journal.id());
		}
						
		String reference = article.piece().reference();
		if(!StringUtils.isBlank(reference));
			object = object.replace("{reference}", reference);
		
		String origin = article.piece().origin();
		if(!StringUtils.isBlank(origin))
			object = object.replace("{origin}", origin);
	
		params.put(dm.objectKey(), object);		
		params.put(dm.orderKey(), order);
		params.put(dm.isPrincipalKey(), order == 1);
		params.put(dm.articleIdKey(), article.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	public void validate() throws IOException {
		
		if(article.piece().isNone())
			throw new IllegalArgumentException("La pièce doit être spécifiée !");
		
		if(all().isEmpty())
			throw new IllegalArgumentException("Il n'y a aucune écriture à passer !");
		
		for (Flux flux : all()) {
			flux.validate();
		}
	}
}
