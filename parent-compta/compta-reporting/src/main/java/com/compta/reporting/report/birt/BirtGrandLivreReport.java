package com.compta.reporting.report.birt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.infrastructure.core.Period;

public final class BirtGrandLivreReport extends BirtComptaReport {

	private transient final Period period;
	private transient final Journal journal;
	
	public BirtGrandLivreReport(Period period, Journal journal, Compta module) {
		super("grand_livre", module);
		
		this.period = period;
		this.journal = journal;
	}

	@Override
	public void render(String format, OutputStream output) throws IOException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		if(!journal.isNone())
			parameters.put("JournalId", journal.id().toString());
		
		period.validate();
		
		parameters.put("Start", java.sql.Date.valueOf(period.start()));
		parameters.put("End", java.sql.Date.valueOf(period.end()));
		
		super.render(module, format, output, parameters);
	}
}
