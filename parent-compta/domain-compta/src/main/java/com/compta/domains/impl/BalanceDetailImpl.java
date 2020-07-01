package com.compta.domains.impl;

import java.io.IOException;

import com.compta.domains.api.Account;
import com.compta.domains.api.Balance;
import com.compta.domains.api.BalanceDetail;

public final class BalanceDetailImpl implements BalanceDetail {

	private transient final Account account;
	private transient final double debit;
	private transient final double credit;
	private transient final double soldeDebiteur;
	private transient final double soldeCrediteur;
	private transient final Balance balance;
	
	public BalanceDetailImpl(Account account, double debit, double credit, double soldeDebiteur, double soldeCrediteur, Balance balance){
		this.account = account;
		this.debit = debit;
		this.credit = credit;
		this.soldeDebiteur = soldeDebiteur;
		this.soldeCrediteur = soldeCrediteur;
		this.balance = balance;
	}
	
	@Override
	public Account account() throws IOException {
		return account;
	}

	@Override
	public double debit() throws IOException {
		return debit;
	}

	@Override
	public double credit() throws IOException {
		return credit;
	}

	@Override
	public double soldeDebiteur() throws IOException {
		return soldeDebiteur;
	}

	@Override
	public double soldeCrediteur() throws IOException {
		return soldeCrediteur;
	}

	@Override
	public Balance balance() throws IOException {
		return balance;
	}
}
