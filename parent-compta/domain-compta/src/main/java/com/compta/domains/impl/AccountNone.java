package com.compta.domains.impl;

import java.io.IOException;

import com.compta.domains.api.Account;
import com.compta.domains.api.AccountChart;
import com.compta.domains.api.AccountType;
import com.compta.domains.api.Lines;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.GuidKeyEntityNone;

public class AccountNone extends GuidKeyEntityNone<Account> implements Account {

	@Override
	public String code() throws IOException {
		return null;
	}

	@Override
	public String name() throws IOException {
		return "Non défini";
	}

	@Override
	public AccountType type() throws IOException {
		return AccountType.NONE;
	}

	@Override
	public AccountChart chart() throws IOException {
		return new ChartNone(); 
	}

	@Override
	public void update(String code, String name, AccountType type, boolean refuseCreditBalance, boolean refuseDebitBalance) throws IOException {
		throw new UnsupportedOperationException("Vous ne pouvez pas modifier un compte inexistant !"); 
	}

	@Override
	public String fullName() throws IOException {
		return "Non défini";
	}

	@Override
	public boolean isAuxiliary() {
		return false;
	}

	@Override
	public TiersType tiersType() throws IOException {
		return new TiersTypeNone();
	}

	@Override
	public Lines lines() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : compte inexistant !");
	}

	@Override
	public boolean refuseCreditBalance() throws IOException {
		return false;
	}

	@Override
	public boolean refuseDebitBalance() throws IOException {
		return false;
	}
}
