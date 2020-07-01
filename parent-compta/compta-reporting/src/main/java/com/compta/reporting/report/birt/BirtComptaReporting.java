package com.compta.reporting.report.birt;

import java.io.IOException;
import java.time.LocalDate;

import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Exercise;
import com.compta.domains.api.Journal;
import com.compta.domains.api.TiersType;
import com.compta.reporting.report.api.ComptaReporting;
import com.infrastructure.core.Period;
import com.infrastructure.core.Report;

public final class BirtComptaReporting implements ComptaReporting {

	private transient final Compta module;
	
	public BirtComptaReporting(Compta module){
		this.module = module;
	}
	
	@Override
	public Report grandLivre(Period period, Journal journal) throws IOException {
		return new BirtGrandLivreReport(period, journal, module);
	}

	@Override
	public Report balanceGenerale(Period period) throws IOException {
		return new BirtBalanceGeneraleReport(period, module);
	}

	@Override
	public Report balanceAuxiliaire(TiersType tiersType, Period period) throws IOException {
		return new BirtBalanceAuxiliaireReport(tiersType, period, module);
	}

	@Override
	public Report balanceAgeeDesTiers(TiersType tiersType, Account auxiliaryAccount, LocalDate startDate) throws IOException {
		return new BirtBalanceAgeeDesTiersReport(tiersType, auxiliaryAccount, startDate, module);
	}

	@Override
	public Report compteResultat(Exercise exercise) throws IOException {
		return new BirtCompteResultatReport(exercise, module);
	}
}
