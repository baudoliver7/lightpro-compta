package com.compta.reporting.report.birt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.compta.domains.api.Compta;
import com.infrastructure.core.Period;

public final class BirtBalanceGeneraleReport extends BirtComptaReport {

	private transient final Period period;
	
	public BirtBalanceGeneraleReport(Period period, Compta module) {
		super("balance_generale", module);
		
		this.period = period;
	}

	@Override
	public void render(String format, OutputStream output) throws IOException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		period.validate();
		
		parameters.put("Start", java.sql.Date.valueOf(period.start()));
		parameters.put("End", java.sql.Date.valueOf(period.end()));
		
		super.render(module, format, output, parameters);
	}
}
