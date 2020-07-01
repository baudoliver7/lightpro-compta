package com.compta.domains.api;

import java.io.IOException;

import com.infrastructure.core.GuidKeyQueryable;

public interface Fluxes extends GuidKeyQueryable<Flux> {
	void deleteAll() throws IOException;
	Flux Principal() throws IOException;
		
	Flux add(String object, JournalType journalType, Journal journal) throws IOException;
	void validate() throws IOException;
}
