package com.compta.domains.api;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface Trame extends Nonable {
	UUID id();
	PieceType pieceType() throws IOException;
	String name() throws IOException;
	TrameFluxes fluxes() throws IOException;	
	
	void update(String name) throws IOException;
	void validate() throws IOException;
	PieceArticle generate(Journal journal, Map<String, Double> params) throws IOException;
	PieceArticle generateReverse(Journal journal, Map<String, Double> params) throws IOException;
	Map<String, Double> buildParams(Double amountBase) throws IOException;
}
