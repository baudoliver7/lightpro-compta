package com.compta.domains.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.impl.AccountDb;
import com.compta.domains.impl.BalanceDetailImpl;
import com.infrastructure.core.Period;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;

public abstract class BalanceBase implements Balance {

	private transient final Base base;
	private transient final boolean useAuxiliaryAccount;
	private transient final TiersType tiersType;
	private transient final Period period;
	private transient final Compta module;
	
	public BalanceBase(Base base, Compta module, Period period, boolean useAuxiliaryAccount, TiersType tiersType){
		this.base = base;
		this.useAuxiliaryAccount = useAuxiliaryAccount;
		this.tiersType = tiersType;
		this.period = period;
		this.module = module;
	}
	
	@Override
	public Period period() throws IOException {
		return period;
	}

	@Override
	public List<BalanceDetail> details() throws IOException {
		
		if(!period.isDefined())
			throw new IllegalArgumentException("La période n'est pas définie !");
		

		AccountMetadata cptDm = AccountMetadata.create();
		LineMetadata lnDm = LineMetadata.create();
		AccountChartMetadata chartDm = AccountChartMetadata.create();
		PieceMetadata pieceDm = PieceMetadata.create();
		FluxMetadata flDm = FluxMetadata.create();
		PieceArticleMetadata paDm = PieceArticleMetadata.create();
		
		List<Object> params = new ArrayList<Object>();
		String statement = String.format("SELECT cpt.%s as cptId, sum(ln.%s) as debit, sum(ln.%s) as credit FROM %s ln "
				+ "{statement-optional} "
				+ "left join %s chart on chart.%s=cpt.%s "
				+ "{statement-auxiliary} "
				+ "join %s fl on fl.%s=ln.%s "
				+ "left join %s pa on pa.%s=fl.%s "
				+ "left join %s piece on piece.%s=pa.%s "				
				+ "where chart.%s = ? AND piece.%s BETWEEN ? AND ? AND piece.%s=? {clause-auxiliary} "
				+ "group by cpt.%s, cpt.%s "
				+ "order by cpt.%s", 
				cptDm.keyName(), lnDm.debitKey(), lnDm.creditKey(), lnDm.domainName(),
				chartDm.domainName(), chartDm.keyName(), cptDm.chartIdKey(),
				flDm.domainName(), flDm.keyName(), lnDm.fluxIdKey(),
				paDm.domainName(), paDm.keyName(), flDm.articleIdKey(),
				pieceDm.domainName(), pieceDm.keyName(), paDm.pieceIdKey(),
				chartDm.moduleIdKey(), pieceDm.pieceDateKey(), pieceDm.statusKey(),
				cptDm.keyName(), cptDm.codeKey(),
				cptDm.codeKey());
		
		params.add(module.id());
		params.add(java.sql.Date.valueOf(period.start()));
		params.add(java.sql.Date.valueOf(period.end()));
		params.add(PieceStatus.ACCOUNTED.id());
		
		if(useAuxiliaryAccount){
			if(tiersType.isNone())
				throw new IllegalArgumentException("Vous devez spécifier le type de tiers concerné !"); 
			
			TiersAccountMetadata tierCptDm = TiersAccountMetadata.create();
			statement = statement.replace("{statement-optional}", String.format("join %s cpt on cpt.%s=ln.%s", cptDm.domainName(), cptDm.keyName(), lnDm.auxiliaryAccountIdKey()));
			statement = statement.replace("{statement-auxiliary}", String.format("left join %s tiercpt on tiercpt.%s=cpt.%s", tierCptDm.domainName(), tierCptDm.keyName(), cptDm.keyName()));
			statement = statement.replace("{clause-auxiliary}", String.format("AND tiercpt.%s = ?", tierCptDm.tiersTypeIdKey()));
			
			params.add(tiersType.id());
		}else{
			statement = statement.replace("{statement-optional}", String.format("join %s cpt on cpt.%s=ln.%s", cptDm.domainName(), cptDm.keyName(), lnDm.generalAccountIdKey()));
			statement = statement.replace("{statement-auxiliary}", "");
			statement = statement.replace("{clause-auxiliary}", "");			
		}
		
		List<BalanceDetail> details = new ArrayList<BalanceDetail>();
		
		DomainsStore ds = base.domainsStore(lnDm);		
		List<Object> results = ds.find(statement, params);
		
		for (int i = 0; i < results.size(); i+=3) {
			UUID cptId = UUIDConvert.fromObject(results.get(i));
			double debit = Double.parseDouble(results.get(i + 1).toString());
			double credit = Double.parseDouble(results.get(i + 2).toString());
			
			double soldeDebiteur = debit > credit ? debit - credit : 0;
			double soldeCrediteur = credit > debit ? credit - debit : 0;
			
			details.add(new BalanceDetailImpl(new AccountDb(base, cptId, module), debit, credit, soldeDebiteur, soldeCrediteur, this));
		}
		
		return details;
	}

	@Override
	public double debit() throws IOException {
		double sum = 0;
		for (BalanceDetail item : details()) {
			sum += item.debit();
		}
		
		return sum;
	}

	@Override
	public double credit() throws IOException {
		double sum = 0;
		for (BalanceDetail item : details()) {
			sum += item.credit();
		}
		
		return sum;
	}

	@Override
	public double soldeDebiteur() throws IOException {
		double sum = 0;
		for (BalanceDetail item : details()) {
			sum += item.soldeDebiteur();
		}
		
		return sum;
	}

	@Override
	public double soldeCrediteur() throws IOException {
		double sum = 0;
		for (BalanceDetail item : details()) {
			sum += item.soldeCrediteur();
		}
		
		return sum;
	}
}
