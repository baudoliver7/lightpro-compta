package com.compta.domains.impl;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Fluxes;
import com.compta.domains.api.Journal;
import com.compta.domains.api.Piece;
import com.compta.domains.api.PieceArticle;
import com.compta.domains.api.TrameFluxes;

public final class PieceArticleTemplate implements PieceArticle {

	private transient final TrameFluxes trame;
	private transient final Journal journal;
	private transient final Map<String, Double> params;
	private transient final UUID id;
	private transient final boolean reverse;
	
	public PieceArticleTemplate(TrameFluxes trame, Journal journal, Map<String, Double> params) {	
		this(trame, journal, params, false);
	}

	public PieceArticleTemplate(TrameFluxes trame, Journal journal, Map<String, Double> params, boolean reverse) {	
		this.trame = trame;
		this.journal = journal;
		this.params = params;
		this.id = UUID.randomUUID();
		this.reverse = reverse;
	}
	
	@Override
	public Piece piece() throws IOException {
		return new PieceNone();
	}

	@Override
	public int order() throws IOException {
		return 0;
	}

	@Override
	public double solde() throws IOException {
		return 0;
	}

	@Override
	public Fluxes fluxes() throws IOException {
		return new FluxesTemplate(journal, trame, params, reverse);
	}

	@Override
	public void validate() throws IOException {
		
	}

	@Override
	public boolean isNone() {
		return false;
	}

	@Override
	public UUID id() {
		return id;
	}
}
