package com.compta.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Flux;
import com.compta.domains.api.Fluxes;
import com.compta.domains.api.Line;
import com.compta.domains.api.Piece;
import com.compta.domains.api.PieceArticle;
import com.compta.domains.api.PieceArticleMetadata;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;

public final class PieceArticleDb extends GuidKeyEntityDb<PieceArticle, PieceArticleMetadata> implements PieceArticle {

	private final Compta module;
	
	public PieceArticleDb(Base base, UUID id, Compta module) {
		super(base, id, "Article de pièce introuvable !");
		this.module = module;
	}

	@Override
	public Piece piece() throws IOException {
		UUID pieceId = ds.get(dm.pieceIdKey());
		return new PieceDb(base, pieceId, module);
	}

	@Override
	public int order() throws IOException {
		return ds.get(dm.orderKey());
	}

	@Override
	public Fluxes fluxes() throws IOException {
		return new FluxesDb(base, this, module);
	}

	@Override
	public void validate() throws IOException {
		fluxes().validate();
	}

	@Override
	public double solde() throws IOException {
		int solde = 0;
		
		Flux mainFlux = fluxes().Principal();
		for (Line line : mainFlux.lines().all()) {
			solde += line.debit();
		}		
		
		return solde;
	}
}
