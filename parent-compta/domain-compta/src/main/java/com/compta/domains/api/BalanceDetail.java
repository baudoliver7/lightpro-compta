package com.compta.domains.api;

import java.io.IOException;

public interface BalanceDetail {
	Account account() throws IOException;
	double debit() throws IOException;
	double credit() throws IOException;
	double soldeDebiteur() throws IOException;
	double soldeCrediteur() throws IOException;
	Balance balance() throws IOException;	
}
