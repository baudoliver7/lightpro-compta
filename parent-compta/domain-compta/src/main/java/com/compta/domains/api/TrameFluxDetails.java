package com.compta.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.infrastructure.core.GuidKeyAdvancedQueryable;

public interface TrameFluxDetails extends GuidKeyAdvancedQueryable<TrameFluxDetail> {
	TrameFluxDetail add(Account generalAccount, OperationSens sens, boolean isAggregateAccount, String formular) throws IOException;
	TrameFluxDetail add(Account generalAccount, OperationSens sens, String formular) throws IOException;
	void deleteAll() throws IOException;
	void validate() throws IOException;
	List<Line> generateLines(Journal journal, Map<String, Double> params) throws IOException;
}
