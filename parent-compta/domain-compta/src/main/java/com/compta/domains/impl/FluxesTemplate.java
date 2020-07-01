package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Flux;
import com.compta.domains.api.Fluxes;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.TrameFluxes;
import com.compta.domains.api.TrameFlux;

public final class FluxesTemplate implements Fluxes {

	private transient final Journal journal;
	private transient final TrameFluxes trame;
	private transient final Map<String, Double> params;
	private transient final boolean reverse;
	
	public FluxesTemplate(Journal journal, TrameFluxes trame, Map<String, Double> params, boolean reverse) {
		this.journal = journal;
		this.trame = trame;
		this.params = params;
		this.reverse = reverse;
	}

	@Override
	public void deleteAll() throws IOException {
		
	}

	@Override
	public Flux Principal() throws IOException {
		return new FluxNone();
	}

	@Override
	public Flux add(String description, JournalType journalType, Journal journal) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : flux inexistant !");
	}

	@Override
	public void validate() throws IOException {

	}

	@Override
	public List<Flux> all() throws IOException {
		List<Flux> fluxes = new ArrayList<Flux>();
		
		for (TrameFlux trFlux : trame.all()) {
			
			Journal fluxJournal = journal;
			if(!trFlux.isPrincipal())
				fluxJournal = trFlux.defaultJournal();
			
			Flux flux = reverse ? trFlux.generateReverse(fluxJournal, params) : trFlux.generate(fluxJournal, params);
			if(!flux.lines().all().isEmpty())
				fluxes.add(flux);
		}
		
		return fluxes;
	}

	@Override
	public boolean contains(Flux item) {
		try {
			return all().stream().anyMatch(m -> m.equals(item));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Flux build(UUID id) {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public long count() throws IOException {
		return all().size();
	}

	@Override
	public Flux get(UUID id) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public void delete(Flux item) throws IOException {

	}
	
	@Override
	public Flux first() throws IOException {
		return all().get(0);
	}

	@Override
	public boolean isEmpty() {
		try {
			return all().isEmpty();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Flux last() throws IOException {
		int size = all().size();
		return all().get(size - 1); 
	}
}
