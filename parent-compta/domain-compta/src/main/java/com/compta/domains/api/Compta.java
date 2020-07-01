package com.compta.domains.api;

import java.io.IOException;
import java.time.LocalDate;

import com.infrastructure.core.Period;
import com.securities.api.Contacts;
import com.securities.api.Module;
import com.securities.api.PaymentModes;
import com.securities.api.Sequences;
import com.securities.api.Taxes;

public interface Compta extends Module {
	
	AccountChart chart() throws IOException;
	Accounts accounts() throws IOException;
	Journals journals() throws IOException;
	PieceTypes pieceTypes() throws IOException;
	PieceTypes pieceTypes(Journal journal) throws IOException;
	TiersTypes tiersTypes() throws IOException;
	Sequences sequences() throws IOException;
	Pieces pieces() throws IOException;	
	Contacts contacts() throws IOException;
	Tierss tiers() throws IOException;
	Exercises exercises() throws IOException;
	int lastMonthExo() throws IOException;
	int lastDayExo() throws IOException;
	Journal journalRan() throws IOException;
	void updateJournalRan(Journal journal) throws IOException;
	Lines lines() throws IOException;	
	Taxes taxes() throws IOException;	
	Reconciles reconciles() throws IOException;
	PaymentModes paymentModes() throws IOException;
	
	// outils comptables
	Balance balanceGenerale(Period period) throws IOException;
	Balance balanceAuxiliaire(TiersType tiersType, Period period) throws IOException;
	BalanceAgeeDesTiers balanceAgeeDesTiers(TiersType tiersType, Account auxiliaryAccount, LocalDate startDate) throws IOException;
	
	ComptaInterfacage interfacage() throws IOException;
	
	void updateLastDayExo(int month, int day) throws IOException;
}
