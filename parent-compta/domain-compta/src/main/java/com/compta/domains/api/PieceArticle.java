package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface PieceArticle extends Nonable {
	UUID id();
	Piece piece() throws IOException;
	int order() throws IOException;
	double solde() throws IOException;
	Fluxes fluxes() throws IOException;
	void validate() throws IOException;
}
