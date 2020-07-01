package com.compta.reporting.report.api;

import java.io.IOException;
import java.time.LocalDate;

import com.compta.domains.api.Account;
import com.compta.domains.api.Exercise;
import com.compta.domains.api.Journal;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.Period;
import com.infrastructure.core.Report;

public interface ComptaReporting {
	Report grandLivre(Period period, Journal journal) throws IOException;
	Report balanceGenerale(Period period) throws IOException;
	Report balanceAuxiliaire(TiersType tiersType, Period period) throws IOException;
	Report balanceAgeeDesTiers(TiersType tiersType, Account auxiliaryAccount, LocalDate startDate) throws IOException;
	Report compteResultat(Exercise exercise) throws IOException;
}
