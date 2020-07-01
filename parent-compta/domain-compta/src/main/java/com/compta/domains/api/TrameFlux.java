package com.compta.domains.api;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface TrameFlux extends Nonable {
	
	UUID id();
	int order() throws IOException;
	String description() throws IOException;	
	JournalType journalType() throws IOException;
	Journal defaultJournal() throws IOException;
	boolean isPrincipal() throws IOException;
	Trame trame() throws IOException;
	TrameFluxDetails details() throws IOException;
	
	void validate() throws IOException;
	Flux generate(Journal journal, Map<String, Double> params) throws IOException;
	Flux generateReverse(Journal journal, Map<String, Double> params) throws IOException;
	Map<String, Double> buildParams(Double amountBase) throws IOException;
}
