package com.compta.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceArticle;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.api.TrameFluxes;
import com.infrastructure.core.GuidKeyEntityNone;

public final class TrameNone extends GuidKeyEntityNone<Trame> implements Trame {

	@Override
	public PieceType pieceType() throws IOException {
		return new PieceTypeNone();
	}

	@Override
	public String name() throws IOException {
		return "Aucun modèle";
	}

	@Override
	public TrameFluxes fluxes() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : modèle de saisie inexistant !");
	}

	@Override
	public void update(String name) throws IOException {
		
	}

	@Override
	public void validate() throws IOException {
		
	}

	@Override
	public PieceArticle generate(Journal journal, Map<String, Double> params) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : modèle de saisie inexistant !");
	}

	@Override
	public Map<String, Double> buildParams(Double amountBase) throws IOException {
		return new HashMap<String, Double>();
	}

	@Override
	public PieceArticle generateReverse(Journal journal, Map<String, Double> params) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : modèle de saisie inexistant !");
	}
}
