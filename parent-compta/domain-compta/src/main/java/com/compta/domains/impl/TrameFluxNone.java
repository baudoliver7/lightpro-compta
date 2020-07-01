package com.compta.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.compta.domains.api.Flux;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.Trame;
import com.compta.domains.api.TrameFluxDetails;
import com.compta.domains.api.TrameFlux;
import com.infrastructure.core.GuidKeyEntityNone;

public final class TrameFluxNone extends GuidKeyEntityNone<TrameFlux> implements TrameFlux {

	@Override
	public int order() throws IOException {
		return 0;
	}

	@Override
	public String description() throws IOException {
		return null;
	}

	@Override
	public JournalType journalType() throws IOException {
		return JournalType.NONE;
	}

	@Override
	public boolean isPrincipal() throws IOException {
		return false;
	}

	@Override
	public Trame trame() throws IOException {
		return new TrameNone();
	}

	@Override
	public void validate() throws IOException {
		
	}

	@Override
	public TrameFluxDetails details() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : trame inexistante !");
	}

	@Override
	public Flux generate(Journal journal, Map<String, Double> params) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : trame inexistante !");
	}

	@Override
	public Journal defaultJournal() throws IOException {
		return new JournalNone();
	}

	@Override
	public Map<String, Double> buildParams(Double amountBase) throws IOException {
		return new HashMap<String, Double>();
	}

	@Override
	public Flux generateReverse(Journal journal, Map<String, Double> params) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : trame inexistante !");
	}
}
