package com.compta.domains.api;

import java.io.IOException;
import java.util.List;

import com.infrastructure.core.Period;

public interface Balance {
	Period period() throws IOException;
	List<BalanceDetail> details() throws IOException;
	double debit() throws IOException;
	double credit() throws IOException;
	double soldeDebiteur() throws IOException;
	double soldeCrediteur() throws IOException;
}
