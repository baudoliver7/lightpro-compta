package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import com.common.utilities.formular.Formular;
import com.compta.domains.api.Account;
import com.compta.domains.api.Flux;
import com.compta.domains.api.Line;
import com.compta.domains.api.OperationSens;
import com.compta.domains.api.Piece;
import com.compta.domains.api.Reconcile;
import com.compta.domains.api.Tiers;

public final class LineTemplate implements Line {

	private final transient Account generalAccount;
	private final transient OperationSens sens;
	private final transient Flux flux;
	private final transient Formular formular;
	private final transient UUID id;
	
	public LineTemplate(Account generalAccount, OperationSens sens, Flux flux, Formular formular, Map<String, Double> params){
		this.generalAccount = generalAccount;
		this.sens = sens;
		this.flux = flux;
		this.formular = formular.withParams(params);
		this.id = UUID.randomUUID();
	}

	@Override
	public UUID id() {
		return id;
	}

	@Override
	public Piece piece() throws IOException {
		return new PieceNone();
	}

	@Override
	public LocalDate dateEcheance() throws IOException {
		return null;
	}

	@Override
	public Account generalAccount() throws IOException {
		return generalAccount;
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
		
		if(sens == OperationSens.DEBIT)
			return formular.result();
		
		return 0;
	}

	@Override
	public double credit() throws IOException {
		
		if(sens == OperationSens.CREDIT)
			return formular.result();
		
		return 0;
	}

	@Override
	public OperationSens sens() throws IOException {
		return sens;
	}

	@Override
	public double balance() throws IOException {
		return debit() - credit();
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
		throw new UnsupportedOperationException("Impossible de concilier une écriture en cours de création !");
	}

	@Override
	public void unreconcile() throws IOException {
		throw new UnsupportedOperationException("Impossible de déconcilier une écriture en cours de création !");
	}

	@Override
	public void lettrer(String lettrage) throws IOException {
		throw new UnsupportedOperationException("Impossible de lettrer une écriture en cours de création !");
	}

	@Override
	public Flux flux() throws IOException {
		return flux;
	}

	@Override
	public void delettrer() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : ligne inexistante !");
	}

	@Override
	public int order() throws IOException {
		return 0;
	}

	@Override
	public boolean isNull() throws IOException {
		return credit() == debit() && credit() == 0;
	}

	@Override
	public boolean isNone() {
		return false;
	}
}
