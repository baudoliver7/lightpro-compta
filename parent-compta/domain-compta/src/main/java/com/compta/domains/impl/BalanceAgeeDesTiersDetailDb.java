package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.compta.domains.api.Account;
import com.compta.domains.api.AccountMetadata;
import com.compta.domains.api.AccountType;
import com.compta.domains.api.BalanceAgeeDesTiersDetail;
import com.compta.domains.api.FluxMetadata;
import com.compta.domains.api.LineMetadata;
import com.compta.domains.api.PieceArticleMetadata;
import com.compta.domains.api.PieceMetadata;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.PieceStatus;
import com.compta.domains.api.PieceTypeMetadata;
import com.compta.domains.api.TiersAccountMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;

public final class BalanceAgeeDesTiersDetailDb implements BalanceAgeeDesTiersDetail {

	private transient final Base base;
	private transient final Account auxiliaryAccount;
	private transient final LocalDate startDate;
	
	public BalanceAgeeDesTiersDetailDb(final Base base, Account auxiliaryAccount, LocalDate startDate){
		this.auxiliaryAccount = auxiliaryAccount;
		this.startDate = startDate;
		this.base = base;
	}
	
	private double calculateAmount(final int daysInf, final int daysSup) throws IOException {
		
		AccountMetadata cptDm = AccountMetadata.create();
		LineMetadata lnDm = LineMetadata.create();
		PieceMetadata pieceDm = PieceMetadata.create();
		PieceTypeMetadata ptDm = PieceTypeMetadata.create();
		FluxMetadata flDm = FluxMetadata.create();
		TiersAccountMetadata tierCptDm = TiersAccountMetadata.create();
		PieceArticleMetadata paDm = PieceArticleMetadata.create();
		
		List<Object> params = new ArrayList<Object>();
		String statement = String.format("SELECT sum(ln.%s) as debit, sum(ln.%s) as credit FROM %s ln "
				+ "join %s cpt on cpt.%s=ln.%s "
				+ "left join %s tiercpt on tiercpt.%s=cpt.%s "
				+ "join %s fl on fl.%s=ln.%s "
				+ "left join %s pa on pa.%s=fl.%s "
				+ "left join %s piece on piece.%s=pa.%s "
				+ "left join %s pt on pt.%s=piece.%s "				
				+ "where cpt.%s=? AND ((pt.%s=? AND piece.%s BETWEEN ? AND ?) OR (pt.%s=? AND piece.%s BETWEEN ? AND ?)) AND ln.%s IS NULL AND piece.%s=? "
				+ "group by cpt.%s", 
				lnDm.debitKey(), lnDm.creditKey(), lnDm.domainName(),
				cptDm.domainName(), cptDm.keyName(), lnDm.auxiliaryAccountIdKey(),
				tierCptDm.domainName(), tierCptDm.keyName(), cptDm.keyName(),
				flDm.domainName(), flDm.keyName(), lnDm.fluxIdKey(),
				paDm.domainName(), paDm.keyName(), flDm.articleIdKey(),
				pieceDm.domainName(), pieceDm.keyName(), paDm.pieceIdKey(),
				ptDm.domainName(), ptDm.keyName(), pieceDm.typeIdKey(),				
				cptDm.keyName(), ptDm.natureIdKey(), pieceDm.dateEcheanceKey(), ptDm.natureIdKey(), pieceDm.pieceDateKey(), lnDm.lettrageKey(), pieceDm.statusKey(),
				cptDm.keyName());
		
		params.add(auxiliaryAccount.id());
		params.add(PieceNature.INVOICE.id());
		params.add(java.sql.Date.valueOf(startDate.plusDays(daysInf)));
		params.add(java.sql.Date.valueOf(startDate.plusDays(daysSup)));		
		params.add(PieceNature.PAYMENT.id());
		params.add(java.sql.Date.valueOf(startDate.plusDays(daysInf)));
		params.add(java.sql.Date.valueOf(startDate.plusDays(daysSup)));
		params.add(PieceStatus.ACCOUNTED.id());
		
		DomainsStore ds = base.domainsStore(lnDm);		
		List<Object> results = ds.find(statement, params);
		
		if(results.isEmpty())
			return 0;
		
		double debit = Double.parseDouble(results.get(0).toString());
		double credit = Double.parseDouble(results.get(1).toString());
		
		if(auxiliaryAccount.type() == AccountType.PAYABLE)
			return credit - debit;
		else
			return debit - credit;
				
	}
	
	@Override
	public Account auxiliaryAccount() throws IOException {
		return auxiliaryAccount;
	}

	@Override
	public double montantNonEchu() throws IOException {
		return calculateAmount(-1000000, -1);
	}

	@Override
	public double montantPeriod0A30() throws IOException {
		return calculateAmount(0, 30);
	}

	@Override
	public double montantPeriod30A60() throws IOException {
		return calculateAmount(31, 60);
	}

	@Override
	public double montantPeriod60A90() throws IOException {
		return calculateAmount(61, 90);
	}

	@Override
	public double montantPeriod90A120() throws IOException {
		return calculateAmount(91, 120);
	}

	@Override
	public double montantPeriodPlus120() throws IOException {
		return calculateAmount(121, 1000000);
	}

	@Override
	public double montantTotal() throws IOException {
		return montantNonEchu() + montantPeriod0A30() + montantPeriod30A60() + montantPeriod60A90() + montantPeriod90A120() + montantPeriodPlus120();
	}
}
