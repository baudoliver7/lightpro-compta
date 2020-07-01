package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;

import com.compta.domains.api.Account;
import com.compta.domains.api.Flux;
import com.compta.domains.api.Line;
import com.compta.domains.api.OperationSens;
import com.compta.domains.api.Piece;
import com.compta.domains.api.Reconcile;
import com.compta.domains.api.Tiers;
import com.infrastructure.core.GuidKeyEntityNone;

public final class LineNone extends GuidKeyEntityNone<Line> implements Line {

	@Override
	public int order() throws IOException {
		return 0;
	}

	@Override
	public Piece piece() throws IOException {
		return new PieceNone();
	}

	@Override
	public Flux flux() throws IOException {
		return new FluxNone();
	}

	@Override
	public LocalDate dateEcheance() throws IOException {
		return null;
	}

	@Override
	public Account generalAccount() throws IOException {
		return new AccountNone();
	}

	@Override
	public Account auxiliaryAccount() throws IOException {
		return new AccountNone();
	}

	@Override
	public Tiers tiers() throws IOException {
		return new TiersNone();
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
	public double balance() throws IOException {
		return 0;
	}

	@Override
	public OperationSens sens() throws IOException {
		return OperationSens.NONE;
	}

	@Override
	public String lettrage() throws IOException {
		return null;
	}

	@Override
	public Reconcile reconcile() throws IOException {
		return new ReconcileNone();
	}

	@Override
	public void reconcile(Reconcile reconcile) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : écriture inexistante !");
	}

	@Override
	public void unreconcile() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : écriture inexistante !");
	}

	@Override
	public void lettrer(String lettrage) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : écriture inexistante !");
	}

	@Override
	public void delettrer() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : écriture inexistante !");
	}

	@Override
	public boolean isNull() throws IOException {
		return true;
	}
}
