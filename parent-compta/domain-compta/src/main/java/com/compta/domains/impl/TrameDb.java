package com.compta.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceArticle;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.api.TrameFlux;
import com.compta.domains.api.TrameFluxes;
import com.compta.domains.api.TrameMetadata;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;

public final class TrameDb extends GuidKeyEntityDb<Trame, TrameMetadata> implements Trame {

	private final Compta module;
	
	public TrameDb(Base base, UUID id, Compta module) {
		super(base, id, "Trame introuvable !");
		this.module = module;
	}

	@Override
	public PieceType pieceType() throws IOException {
		UUID pieceTypeId = ds.get(dm.pieceTypeIdKey());
		return new PieceTypeDb(base, pieceTypeId, module);
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public TrameFluxes fluxes() throws IOException {
		return new TrameFluxesDb(base, this, module);
	}

	@Override
	public void update(String name) throws IOException {
		
		if(StringUtils.isBlank(name))
			throw new IllegalArgumentException("Le libellé du modèle de saisie doit être renseigné !");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);	
		
		ds.set(params);	
	}

	@Override
	public void validate() throws IOException {
		fluxes().validate();
	}

	@Override
	public PieceArticle generate(Journal journal, Map<String, Double> params) throws IOException {
		return fluxes().generate(journal, params);
	}

	@Override
	public Map<String, Double> buildParams(Double amountBase) throws IOException {
		Map<String, Double> params = new HashMap<String, Double>();
		
		for (TrameFlux flux : fluxes().all()) {
			Map<String, Double> paramsi = flux.buildParams(amountBase);
			
			for (Entry<String, Double> param : paramsi.entrySet()) {
				if(!params.containsKey(param.getKey()))
					params.put(param.getKey(), param.getValue());
			}
		}
		
		return params;
	}

	@Override
	public PieceArticle generateReverse(Journal journal, Map<String, Double> params) throws IOException {
		return fluxes().generateReverse(journal, params);
	}
}
