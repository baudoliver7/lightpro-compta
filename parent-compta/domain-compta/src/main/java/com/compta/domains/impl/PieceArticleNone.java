package com.compta.domains.impl;

import java.io.IOException;

import com.compta.domains.api.Fluxes;
import com.compta.domains.api.Piece;
import com.compta.domains.api.PieceArticle;
import com.infrastructure.core.GuidKeyEntityNone;

public final class PieceArticleNone extends GuidKeyEntityNone<PieceArticle> implements PieceArticle {

	@Override
	public Piece piece() throws IOException {
		return new PieceNone();
	}

	@Override
	public int order() throws IOException {
		return 0;
	}

	@Override
	public Fluxes fluxes() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : article de pièce inexistant !");
	}

	@Override
	public void validate() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : article de pièce inexistant !");
	}

	@Override
	public double solde() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : article de pièce inexistant !");
	}
}
