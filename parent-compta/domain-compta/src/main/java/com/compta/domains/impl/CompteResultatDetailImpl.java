package com.compta.domains.impl;

import java.io.IOException;

import com.compta.domains.api.Account;
import com.compta.domains.api.CompteResultat;
import com.compta.domains.api.CompteResultatDetail;
import com.compta.domains.api.Exercise;
import com.compta.domains.api.PieceStatus;

public final class CompteResultatDetailImpl implements CompteResultatDetail {

	private transient final Exercise exercise;
	private transient final Account account;
	
	public CompteResultatDetailImpl(final Account account, final Exercise exercise){
		this.exercise = exercise;
		this.account = account;
	}
	
	@Override
	public String code() throws IOException {
		return account.code();
	}

	@Override
	public String name() throws IOException {
		return account.name();
	}

	@Override
	public double amount() throws IOException {
		double amount = account.lines().of(PieceStatus.ACCOUNTED).of(exercise.period()).balance();
		
		switch (account.type()) {
		case OPERATING_EXPENSES:
		case FINANCIAL_EXPENSES:
		case EXTRA_EXPENSES:
			amount = - amount;
			break;

		default:
			break;
		}
		
		return amount;
	}

	@Override
	public CompteResultat compteResultat() throws IOException {
		return new CompteResultatImpl(exercise);
	}
}
