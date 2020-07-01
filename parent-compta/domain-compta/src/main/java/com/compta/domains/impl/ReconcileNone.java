package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Line;
import com.compta.domains.api.Reconcile;
import com.infrastructure.core.GuidKeyEntityNone;

public final class ReconcileNone extends GuidKeyEntityNone<Reconcile> implements Reconcile {

	@Override
	public Account auxiliaryAccount() throws IOException {
		return new AccountNone();
	}

	@Override
	public List<Line> invoiceLines() throws IOException {
		return new ArrayList<Line>(); 
	}

	@Override
	public List<Line> paymentLines() throws IOException {
		return new ArrayList<Line>();
	}

	@Override
	public double debit() throws IOException {
		return 0;
	}

	@Override
	public double credit() throws IOException {
		return 0;
	}

	@Override
	public boolean isLettred() {
		return false;
	}

	@Override
	public void add(Line line) throws IOException {
		throw new UnsupportedOperationException("L'�criture ne peut pas �tre concili�e dans une correspondance inexistante !");
	}

	@Override
	public void remove(Line line) throws IOException {
		throw new UnsupportedOperationException("L'�criture ne peut pas �tre retir�e d'une correspondance inexistante !");
	}

	@Override
	public void lettrer() throws IOException {
		throw new UnsupportedOperationException("Impossible de lettrer les �critures d'une correspondance inexistante !");
	}

	@Override
	public Compta module() throws IOException {
		return new ComptaNone();
	}

	@Override
	public void updateAmounts() throws IOException {
		throw new UnsupportedOperationException("Impossible de mettre � jour les sommes des montants au d�bit et cr�dit pour une correspondance inexistante !");
	}

	@Override
	public List<Line> allLines() throws IOException {
		return new ArrayList<Line>();
	}

	@Override
	public void removeAll() throws IOException {
		throw new UnsupportedOperationException("Aucune �criture ne peut pas �tre retir�e d'une correspondance inexistante !");
	}

	@Override
	public void delettrer() throws IOException {
		throw new UnsupportedOperationException("Op�ration non support�e : correspondance inexistante !");
	}
}
