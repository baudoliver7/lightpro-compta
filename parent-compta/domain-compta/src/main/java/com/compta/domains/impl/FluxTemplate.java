package com.compta.domains.impl;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Flux;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.Lines;
import com.compta.domains.api.PieceArticle;
import com.compta.domains.api.TrameFlux;

public final class FluxTemplate implements Flux {

	private transient final Journal journal;
	private transient final TrameFlux trameFlux;
	private transient final Map<String, Double> params;
	private transient final UUID id;
	private transient final boolean reverse;
	
	public FluxTemplate(Journal journal, TrameFlux trameFlux, Map<String, Double> params) {
		this(journal, trameFlux, params, false);
	}
	
	public FluxTemplate(Journal journal, TrameFlux trameFlux, Map<String, Double> params, boolean reverse) {
		this.journal = journal;
		this.trameFlux = trameFlux;
		this.params = params;
		this.id = UUID.randomUUID();
		this.reverse = reverse;
	}
	
	@Override
	public UUID id(){
		return id;
	}

	@Override
	public String object() throws IOException {
		return trameFlux.description();
	}

	@Override
	public JournalType journalType() throws IOException {
		return journal().type();
	}

	@Override
	public Journal journal() throws IOException {
		if(!journal.isNone())
			return journal;
		
		return trameFlux.defaultJournal();
	}

	@Override
	public int order() throws IOException {
		return trameFlux.order();
	}

	@Override
	public boolean isPrincipal() throws IOException {
		return trameFlux.isPrincipal();
	}

	@Override
	public Lines lines() throws IOException {
		return new LinesTemplate(trameFlux, journal, params, reverse);
	}

	@Override
	public void validate() throws IOException {
		lines().validate();
	}

	@Override
	public double debit() throws IOException {
		return 0;
	}

	@Override
	public double credit() throws IOException {
		return 0;
	}

	@Override
	public PieceArticle article() throws IOException {
		return new PieceArticleNone();
	}

	@Override
	public boolean isNone() {
		return false;
	}
}
