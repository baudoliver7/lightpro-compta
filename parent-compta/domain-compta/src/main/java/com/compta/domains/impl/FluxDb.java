package com.compta.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Flux;
import com.compta.domains.api.FluxMetadata;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.Lines;
import com.compta.domains.api.PieceArticle;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;

public final class FluxDb extends GuidKeyEntityDb<Flux, FluxMetadata> implements Flux {

	private final Compta module;
	
	public FluxDb(Base base, UUID id, Compta module) {
		super(base, id, "Flux introuvable !");
		this.module = module;
	}

	@Override
	public String object() throws IOException {
		return ds.get(dm.objectKey());
	}

	@Override
	public JournalType journalType() throws IOException {
		int typeId = ds.get(dm.journalTypeIdKey());
		return JournalType.get(typeId);
	}

	@Override
	public Journal journal() throws IOException {
		UUID journalId = ds.get(dm.journalIdKey());
		return new JournalDb(base, journalId, module);
	}

	@Override
	public PieceArticle article() throws IOException {
		UUID articleId = ds.get(dm.articleIdKey());
		return new PieceArticleDb(base, articleId, module);
	}

	@Override
	public int order() throws IOException {
		return ds.get(dm.orderKey());
	}

	@Override
	public boolean isPrincipal() throws IOException {
		return ds.get(dm.isPrincipalKey());
	}

	@Override
	public Lines lines() throws IOException {
		return article().piece().type().module().lines().of(this);
	}

	@Override
	public void validate() throws IOException {
		
		double debit = debit();
		double credit = credit();
		
		if(debit != credit)
			throw new IllegalArgumentException("Les montants au débit et au crédit doivent être égaux !");
		
		if(debit == 0)
			throw new IllegalArgumentException("Il n'y a aucun montant à comptabiliser !");
		
		lines().validate();
	}

	@Override
	public double debit() throws IOException {
		return lines().debit();
	}

	@Override
	public double credit() throws IOException {
		return lines().credit();
	}
}
