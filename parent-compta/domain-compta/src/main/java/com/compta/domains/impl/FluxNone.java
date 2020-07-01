package com.compta.domains.impl;

import java.io.IOException;

import com.compta.domains.api.Flux;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.Lines;
import com.compta.domains.api.PieceArticle;
import com.infrastructure.core.GuidKeyEntityNone;

public final class FluxNone extends GuidKeyEntityNone<Flux> implements Flux {

	@Override
	public String object() throws IOException {
		return null;
	}

	@Override
	public JournalType journalType() throws IOException {
		return JournalType.NONE;
	}

	@Override
	public Journal journal() throws IOException {
		return new JournalNone();
	}

	@Override
	public PieceArticle article() throws IOException {
		return new PieceArticleNone();
	}

	@Override
	public int order() throws IOException {
		return 0;
	}

	@Override
	public boolean isPrincipal() throws IOException {
		return false;
	}

	@Override
	public Lines lines() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : flux inexistant !");
	}

	@Override
	public void validate() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : flux inexistant !");
	}

	@Override
	public double debit() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : flux inexistant !");
	}

	@Override
	public double credit() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : flux inexistant !");
	}
}
