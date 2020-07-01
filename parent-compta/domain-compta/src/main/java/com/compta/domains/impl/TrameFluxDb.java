package com.compta.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Flux;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.Trame;
import com.compta.domains.api.TrameFluxDetails;
import com.compta.domains.api.TrameFluxMetadata;
import com.compta.domains.api.TrameFlux;
import com.compta.domains.api.TrameFluxDetail;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;

public final class TrameFluxDb extends GuidKeyEntityDb<TrameFlux, TrameFluxMetadata> implements TrameFlux {

	private final Compta module;
	
	public TrameFluxDb(Base base, UUID id, Compta module) {
		super(base, id, "Flux de trame introuvable !");
		this.module = module;
	}

	@Override
	public int order() throws IOException {
		return ds.get(dm.orderKey());
	}

	@Override
	public String description() throws IOException {
		return ds.get(dm.descriptionKey());
	}

	@Override
	public JournalType journalType() throws IOException {
		int typeId = ds.get(dm.journalTypeIdKey());
		return JournalType.get(typeId);
	}

	@Override
	public boolean isPrincipal() throws IOException {
		return ds.get(dm.isPrincipalKey());
	}

	@Override
	public Trame trame() throws IOException {
		UUID trameId = ds.get(dm.trameIdKey());
		return new TrameDb(base, trameId, module);
	}

	@Override
	public void validate() throws IOException {
		details().validate();
	}

	@Override
	public TrameFluxDetails details() throws IOException {
		return new TrameFluxDetailsDb(base, this, module);
	}

	@Override
	public Flux generate(Journal journal, Map<String, Double> params) throws IOException {
		return new FluxTemplate(journal, this, params);
	}

	@Override
	public Journal defaultJournal() throws IOException {
		UUID journalId = ds.get(dm.defaultJournalIdKey());
		
		if(journalId == null)
			return new JournalNone();
		else
			return new JournalDb(base, journalId, module);
	}

	@Override
	public Map<String, Double> buildParams(Double amountBase) throws IOException {
		Map<String, Double> params = new HashMap<String, Double>();
		
		for (TrameFluxDetail detail : details().all()) {
			Map<String, Double> paramsi = detail.buildParams(amountBase);
			
			for (Entry<String, Double> param : paramsi.entrySet()) {
				if(!params.containsKey(param.getKey()))
					params.put(param.getKey(), param.getValue());
			}
		}
		
		return params;
	}

	@Override
	public Flux generateReverse(Journal journal, Map<String, Double> params) throws IOException {
		return new FluxTemplate(journal, this, params, true);
	}
}
