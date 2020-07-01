package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.FluxMetadata;
import com.compta.domains.api.Line;
import com.compta.domains.api.LineMetadata;
import com.compta.domains.api.PieceArticleMetadata;
import com.compta.domains.api.PieceMetadata;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.PieceTypeMetadata;
import com.compta.domains.api.Reconcile;
import com.compta.domains.api.ReconcileMetadata;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;

public final class ReconcileDb extends GuidKeyEntityDb<Reconcile, ReconcileMetadata> implements Reconcile {

	private final Compta module;
	
	public ReconcileDb(Base base, UUID id, Compta module) {
		super(base, id, "Correspondance introuvable !");
		this.module = module;
	}

	@Override
	public Account auxiliaryAccount() throws IOException {
		UUID accountId = ds.get(dm.auxiliaryAccountIdKey());
		return new AccountDb(base, accountId, module);
	}

	List<Line> searchLines(PieceNature nature) throws IOException {
		
		List<Object> params = new ArrayList<Object>();

		LineMetadata lnDm = LineMetadata.create();
	    PieceMetadata pDm = PieceMetadata.create();
		PieceTypeMetadata ptDm = PieceTypeMetadata.create();
		FluxMetadata flDm = FluxMetadata.create();
		PieceArticleMetadata paDm = PieceArticleMetadata.create();
		
		String statement = String.format("SELECT ln.%s FROM %s ln "
				+ "JOIN %s fl ON fl.%s=ln.%s "
				+ "JOIN %s pa ON pa.%s = fl.%s "
				+ "left JOIN %s p ON p.%s = pa.%s "
				+ "left JOIN %s pt ON pt.%s = p.%s "
				+ "JOIN %s rec ON rec.%s = ln.%s "
				+ "WHERE rec.%s=? AND ln.%s=?",				 
				lnDm.keyName(), lnDm.domainName(), 
				flDm.domainName(), flDm.keyName(), lnDm.fluxIdKey(),
				paDm.domainName(), paDm.keyName(), flDm.articleIdKey(),
				pDm.domainName(), pDm.keyName(), paDm.pieceIdKey(),
				ptDm.domainName(), ptDm.keyName(), pDm.typeIdKey(),
				dm.domainName(), dm.keyName(), lnDm.reconcileIdKey(),
				dm.moduleIdKey(), lnDm.reconcileIdKey());
		
		params.add(module().id());
		params.add(id);
		
		if(nature != PieceNature.NONE){
			statement = String.format("%s AND pt.%s=?", statement, ptDm.natureIdKey());
			params.add(nature.id());
		}		
		
		statement = String.format("%s ORDER BY pt.%s ASC", statement, ptDm.natureIdKey());
		DomainsStore ds = base.domainsStore(lnDm);
		
		return  ds.find(statement, params)
				  .stream()
				  .map(m -> new LineDb(base, UUIDConvert.fromObject(m), module))
				  .collect(Collectors.toList());
	}
	
	@Override
	public List<Line> invoiceLines() throws IOException {
		return searchLines(PieceNature.INVOICE);		
	}

	@Override
	public List<Line> paymentLines() throws IOException {
		return searchLines(PieceNature.PAYMENT);
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
	public boolean isLettred() {
		try {
			return ds.get(dm.lettratedKey());
		} catch (IOException e) {			
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}
	
	@Override
	public void add(Line line) throws IOException {
		
		if(isLettred())
			throw new IllegalArgumentException("La correspondance est déjà lettrée !");
		
		line.reconcile(this);
	}

	@Override
	public void remove(Line line) throws IOException {
		if(!line.reconcile().isNone() && line.reconcile().equals(this))
		{
			if(allLines().size() == 1)
				throw new IllegalArgumentException("Vous ne pouvez pas supprimer la dernière ligne de la correspondance !");
			
			line.unreconcile();
		}
	}

	@Override
	public void lettrer() throws IOException {
		
		if(isLettred())
			throw new IllegalArgumentException("La correspondance est déjà lettrée !");
		
		// 1 - valider le lettrage
		if(debit() != credit())
			throw new IllegalArgumentException("Le somme des débits n'est pas égale à la somme des crédits !");
		
		// 2 - lettrer
		TiersType tiersType = auxiliaryAccount().tiersType();
		String lettrage = tiersType.sequenceLettrage().generate();
		
		for (Line line : allLines()) {
			line.lettrer(lettrage);
		}
		
		ds.set(dm.lettratedKey(), true);
	}

	@Override
	public Compta module() throws IOException {
		return module;
	}
	
	@Override
	public void updateAmounts() throws IOException {
		
		double debit = 0;
		double credit = 0;
		
		for (Line invoice : invoiceLines()) {
			debit += invoice.debit();
			credit += invoice.credit();
		}
		
		for (Line payment : paymentLines()) {
			debit += payment.debit();
			credit += payment.credit();
		}
		
		ds.set(dm.debitKey(), debit);
		ds.set(dm.creditKey(), credit);
	}

	@Override
	public List<Line> allLines() throws IOException {
		return searchLines(PieceNature.NONE);
	}

	@Override
	public void removeAll() throws IOException {
		for (Line line : allLines()) {
			remove(line);
		}
	}

	@Override
	public void delettrer() throws IOException {
		
		if(!isLettred())
			throw new IllegalArgumentException("La correspondance est déjà delettrée !");
		
		// 1 - lettrer
		for (Line line : allLines()) {
			line.delettrer();
		}
		
		ds.set(dm.lettratedKey(), false);
	}
}
