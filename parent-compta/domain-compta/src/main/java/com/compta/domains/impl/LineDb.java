package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Flux;
import com.compta.domains.api.Line;
import com.compta.domains.api.LineMetadata;
import com.compta.domains.api.OperationSens;
import com.compta.domains.api.Piece;
import com.compta.domains.api.Reconcile;
import com.compta.domains.api.Tiers;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;

public final class LineDb extends GuidKeyEntityDb<Line, LineMetadata> implements Line {
	
	private final Compta module;
	
	public LineDb(final Base base, final UUID id, final Compta module){
		super(base, id, "Ecriture introuvable !");
		this.module = module;
	}
	
	@Override
	public Account generalAccount() throws IOException {
		UUID accountId = ds.get(dm.generalAccountIdKey());
		return flux().journal().module().accounts().get(accountId);
	}

	@Override
	public Account auxiliaryAccount() throws IOException {
		UUID accountId = ds.get(dm.auxiliaryAccountIdKey());		
		return flux().journal().module().accounts().build(accountId);
	}

	@Override
	public Tiers tiers() throws IOException {
		return piece().tiers();
	}

	@Override
	public double debit() throws IOException {
		return ds.get(dm.debitKey());
	}

	@Override
	public double credit() throws IOException {
		return ds.get(dm.creditKey());
	}

	@Override
	public Flux flux() throws IOException {
		UUID fluxId = ds.get(dm.fluxIdKey());
		return new FluxDb(base, fluxId, module);
	}

	@Override
	public LocalDate dateEcheance() throws IOException {
		return piece().dateEcheance();
	}

	@Override
	public OperationSens sens() throws IOException {
		
		if(debit() > 0){
			return OperationSens.DEBIT;
		}else {
			return OperationSens.CREDIT;
		}		
	}

	@Override
	public double balance() throws IOException {
		return debit() - credit();
	}

	@Override
	public String lettrage() throws IOException {
		return ds.get(dm.lettrageKey());
	}

	@Override
	public Reconcile reconcile() throws IOException {
		UUID reconcileId = ds.get(dm.reconcileIdKey());
		return piece().type().module().reconciles().build(reconcileId);
	}

	@Override
	public void reconcile(Reconcile reconcile) throws IOException {
			
		if(!piece().isInvoiceOrPayment())
			throw new IllegalArgumentException("La correspondance des écritures ne concerne que les factures ou les règlements !");
		
		if(reconcile().id() != null && reconcile.isLettred())
			throw new IllegalArgumentException(String.format("L'écriture '%s' a déjà été lettrée !", flux().object()));
		
		if(reconcile.id() == null)
			throw new IllegalArgumentException(String.format("Correspondance impossible à réaliser pour l'écriture '%s' ", this.flux().object()));
			
		ds.set(dm.reconcileIdKey(), reconcile.id());
		reconcile().updateAmounts();
	}

	@Override
	public void unreconcile() throws IOException {
		Reconcile reconcile = reconcile();
		
		if(reconcile.id() != null){
			ds.set(dm.reconcileIdKey(), null);
			reconcile.updateAmounts();
		}
	}

	@Override
	public void lettrer(String lettrage) throws IOException {
		
		if(StringUtils.isBlank(lettrage))
			throw new IllegalArgumentException("Lettrage invalide : vous devez fournir un lettrage non vide !");
		
		ds.set(dm.lettrageKey(), lettrage);
	}

	@Override
	public Piece piece() throws IOException {
		return flux().article().piece();
	}

	@Override
	public void delettrer() throws IOException {
		ds.set(dm.lettrageKey(), null); 
	}

	@Override
	public int order() throws IOException {
		return ds.get(dm.orderKey());
	}

	@Override
	public boolean isNull() throws IOException {
		return credit() == debit() && credit() == 0;
	}
}
