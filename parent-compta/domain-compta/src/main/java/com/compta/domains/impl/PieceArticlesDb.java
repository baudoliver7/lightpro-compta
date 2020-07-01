package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Flux;
import com.compta.domains.api.Line;
import com.compta.domains.api.Piece;
import com.compta.domains.api.PieceArticle;
import com.compta.domains.api.PieceArticleMetadata;
import com.compta.domains.api.PieceArticles;
import com.infrastructure.core.GuidKeyQueryableDb;
import com.infrastructure.datasource.Base;

public final class PieceArticlesDb extends GuidKeyQueryableDb<PieceArticle, PieceArticleMetadata> implements PieceArticles {

	private transient final Piece piece;
	private final Compta module;
	
	public PieceArticlesDb(Base base, Piece piece, Compta module) {
		super(base, "Article de pièce introuvable !");
		
		this.piece = piece;
		this.module = module;
	}

	@Override
	public void delete(PieceArticle item) throws IOException {
		if(piece.isNone())
			throw new IllegalArgumentException("Aucune pièce n'est spécifiée !");
		
		if(contains(item)){
			item.fluxes().deleteAll();
			ds.delete(item.id());
		}
	}
	
	@Override
	public void deleteAll() throws IOException {
		for (PieceArticle item : all()) {
			delete(item);
		}
	}
	
	@Override
	public PieceArticle add() throws IOException {
		
		if(piece.isNone())
			throw new IllegalArgumentException("Aucune pièce n'est spécifiée !"); 
		
		int order = all().size() + 1;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.orderKey(), order);
		params.put(dm.pieceIdKey(), piece.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	public void validate() throws IOException {
		
		if(piece.isNone())
			throw new IllegalArgumentException("Aucune pièce n'est spécifiée !");

		if(all().isEmpty())
			throw new IllegalArgumentException("Il n'y a aucune écriture à passer !");
		
		for (PieceArticle article : all()) {
			article.validate();
		}
	}

	@Override
	public List<PieceArticle> all() throws IOException {
		
		List<Object> params = new ArrayList<Object>();

		String statement = String.format("%s pa "
				+ "WHERE pa.%s=?",				 
				dm.domainName(), 
				dm.pieceIdKey());
		
		params.add(piece.id());
		
		String orderClause = String.format("ORDER BY pa.%s ASC", dm.orderKey());
		
		String keyResult = String.format("pa.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause)
				   .find()
				   .stream()
				   .map(m -> newOne(UUIDConvert.fromObject(m)))
				   .collect(Collectors.toList());
	}

	@Override
	public boolean contains(PieceArticle item) {
		try {
			return !item.isNone() && item.piece().equals(piece);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	protected PieceArticle newOne(UUID id) {
		return new PieceArticleDb(base, id, module);
	}

	@Override
	public PieceArticle none() {
		return new PieceArticleNone();
	}

	@Override
	public PieceArticle add(PieceArticle article) throws IOException {
		PieceArticle articleCopy = add();
		
		for (Flux flux : article.fluxes().all()) {	
			
			if(flux.lines().all().isEmpty())
				continue;
			
			Flux fluxCopy = articleCopy.fluxes().add(flux.object(), flux.journalType(), flux.journal());
			
			for (Line line : flux.lines().all()) {
				fluxCopy.lines().add(piece.tiers(), line.generalAccount(), line.debit(), line.credit());
			}
		}
		
		return articleCopy;
	}
}
