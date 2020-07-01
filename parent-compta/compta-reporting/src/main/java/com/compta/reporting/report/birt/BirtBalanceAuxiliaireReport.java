package com.compta.reporting.report.birt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.compta.domains.api.Compta;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.Period;

public final class BirtBalanceAuxiliaireReport extends BirtComptaReport {

	private transient final TiersType tiersType;
	private transient final Period period;
	
	public BirtBalanceAuxiliaireReport(TiersType tiersType, Period period, Compta module) {
		super("balance_auxiliaire", module);
		
		this.period = period;
		this.tiersType = tiersType;
	}

	@Override
	public void render(String format, OutputStream output) throws IOException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		period.validate();
		
		parameters.put("TiersTypeId", tiersType.id().toString());
		parameters.put("Start", java.sql.Date.valueOf(period.start()));
		parameters.put("End", java.sql.Date.valueOf(period.end()));
		
		super.render(module, format, output, parameters);
	}
}
