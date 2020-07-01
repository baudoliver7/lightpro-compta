package com.compta.domains.api;

import java.io.IOException;
import java.util.Map;

import com.infrastructure.core.GuidKeyAdvancedQueryable;

public interface TrameFluxes extends GuidKeyAdvancedQueryable<TrameFlux> {
	public abstract TrameFlux add(String description, JournalType journalType, Journal defaultJournal) throws IOException;
	public abstract void deleteAll() throws IOException;
	
	public abstract void validate() throws IOException;
	public abstract PieceArticle generate(Journal journal, Map<String, Double> params) throws IOException;
	public abstract PieceArticle generateReverse(Journal journal, Map<String, Double> params) throws IOException;
}
