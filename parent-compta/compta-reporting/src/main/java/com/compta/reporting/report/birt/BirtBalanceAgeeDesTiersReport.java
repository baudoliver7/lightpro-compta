package com.compta.reporting.report.birt;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.TiersType;

public final class BirtBalanceAgeeDesTiersReport extends BirtComptaReport {

	private transient final TiersType tiersType;
	private transient final Account auxiliaryAccount;
	private transient final LocalDate startDate;
	
	public BirtBalanceAgeeDesTiersReport(TiersType tiersType, Account auxiliaryAccount, LocalDate startDate, Compta module) {
		super("balance_agee_des_tiers", module);
		
		this.auxiliaryAccount = auxiliaryAccount;
		this.startDate = startDate;
		this.tiersType = tiersType;
	}

	@Override
	public void render(String format, OutputStream output) throws IOException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		if(!auxiliaryAccount.isNone() && !auxiliaryAccount.isAuxiliary())
			throw new IllegalArgumentException("Le compte spécifié doit être un compte auxiliaire !");
		
		parameters.put("TiersTypeId", tiersType.id().toString());
		parameters.put("Start", java.sql.Date.valueOf(startDate));
		parameters.put("AuxiliaryAccountId", auxiliaryAccount.id());
		
		super.render(module, format, output, parameters);
	}
}
