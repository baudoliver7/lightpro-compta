package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.compta.domains.api.Account;
import com.compta.domains.api.AccountChart;
import com.compta.domains.api.Accounts;
import com.compta.domains.api.Balance;
import com.compta.domains.api.BalanceAgeeDesTiers;
import com.compta.domains.api.Compta;
import com.compta.domains.api.ComptaInterfacage;
import com.compta.domains.api.Exercises;
import com.compta.domains.api.Journal;
import com.compta.domains.api.Journals;
import com.compta.domains.api.Lines;
import com.compta.domains.api.PieceTypes;
import com.compta.domains.api.Pieces;
import com.compta.domains.api.Reconciles;
import com.compta.domains.api.TiersType;
import com.compta.domains.api.TiersTypes;
import com.compta.domains.api.Tierss;
import com.infrastructure.core.EntityNone;
import com.infrastructure.core.Period;
import com.securities.api.Company;
import com.securities.api.Contacts;
import com.securities.api.Feature;
import com.securities.api.FeatureSubscribed;
import com.securities.api.Features;
import com.securities.api.Indicators;
import com.securities.api.Log;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.securities.api.PaymentModes;
import com.securities.api.Sequences;
import com.securities.api.Taxes;
import com.securities.impl.CompanyNone;

public final class ComptaNone extends EntityNone<Compta, UUID> implements Compta {

	@Override
	public void activate(boolean arg0) throws IOException {

	}

	@Override
	public Company company() throws IOException {
		return new CompanyNone();
	}

	@Override
	public String description() throws IOException {
		return null;
	}

	@Override
	public Features featuresAvailable() throws IOException {
		
		return null;
	}

	@Override
	public Features featuresProposed() throws IOException {
		
		return null;
	}

	@Override
	public Features featuresSubscribed() throws IOException {
		
		return null;
	}

	@Override
	public Indicators indicators() throws IOException {
		
		return null;
	}

	@Override
	public Module install() throws IOException {
		
		return null;
	}

	@Override
	public boolean isActive() {
		
		return false;
	}

	@Override
	public boolean isInstalled() {
		
		return false;
	}

	@Override
	public boolean isSubscribed() {
		
		return false;
	}

	@Override
	public String name() throws IOException {
		
		return null;
	}

	@Override
	public int order() throws IOException {
		
		return 0;
	}

	@Override
	public String shortName() throws IOException {
		
		return null;
	}

	@Override
	public Module subscribe() throws IOException {
		
		return null;
	}

	@Override
	public FeatureSubscribed subscribeTo(Feature arg0) throws IOException {
		
		return null;
	}

	@Override
	public ModuleType type() throws IOException {
		
		return null;
	}

	@Override
	public Module uninstall() throws IOException {
		
		return null;
	}

	@Override
	public Module unsubscribe() throws IOException {
		
		return null;
	}

	@Override
	public void unsubscribeTo(Feature arg0) throws IOException {
		

	}

	@Override
	public boolean isNone() {
		
		return false;
	}

	@Override
	public AccountChart chart() throws IOException {
		
		return null;
	}

	@Override
	public Accounts accounts() throws IOException {
		
		return null;
	}

	@Override
	public Journals journals() throws IOException {
		
		return null;
	}

	@Override
	public PieceTypes pieceTypes() throws IOException {
		
		return null;
	}

	@Override
	public PieceTypes pieceTypes(Journal journal) throws IOException {
		
		return null;
	}

	@Override
	public TiersTypes tiersTypes() throws IOException {
		
		return null;
	}

	@Override
	public Sequences sequences() throws IOException {
		
		return null;
	}

	@Override
	public Pieces pieces() throws IOException {
		
		return null;
	}

	@Override
	public Contacts contacts() throws IOException {
		
		return null;
	}

	@Override
	public Tierss tiers() throws IOException {
		
		return null;
	}

	@Override
	public Exercises exercises() throws IOException {
		
		return null;
	}

	@Override
	public int lastMonthExo() throws IOException {
		
		return 0;
	}

	@Override
	public int lastDayExo() throws IOException {
		
		return 0;
	}

	@Override
	public Journal journalRan() throws IOException {
		
		return null;
	}

	@Override
	public void updateJournalRan(Journal journal) throws IOException {
		

	}

	@Override
	public Lines lines() throws IOException {
		
		return null;
	}

	@Override
	public Taxes taxes() throws IOException {
		
		return null;
	}

	@Override
	public Reconciles reconciles() throws IOException {
		
		return null;
	}

	@Override
	public PaymentModes paymentModes() throws IOException {
		
		return null;
	}

	@Override
	public Balance balanceGenerale(Period period) throws IOException {
		
		return null;
	}

	@Override
	public Balance balanceAuxiliaire(TiersType tiersType, Period period) throws IOException {
		
		return null;
	}

	@Override
	public BalanceAgeeDesTiers balanceAgeeDesTiers(TiersType tiersType, Account auxiliaryAccount, LocalDate startDate)
			throws IOException {
		
		return null;
	}

	@Override
	public ComptaInterfacage interfacage() throws IOException {	
		return null;
	}

	@Override
	public void updateLastDayExo(int month, int day) throws IOException {
		
	}

	@Override
	public Log log() throws IOException {
		return null;
	}
}
