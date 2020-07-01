package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Account;
import com.compta.domains.api.AccountMetadata;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Line;
import com.compta.domains.api.Reconcile;
import com.compta.domains.api.ReconcileMetadata;
import com.compta.domains.api.ReconcileStatus;
import com.compta.domains.api.Reconciles;
import com.compta.domains.api.TiersAccountMetadata;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;

public final class ReconcilesDb extends GuidKeyAdvancedQueryableDb<Reconcile, ReconcileMetadata> implements Reconciles {

	private transient final Compta module;
	private transient final TiersType tiersType;
	private transient final Account auxiliaryAccount;
	private transient final ReconcileStatus status;
	
	public ReconcilesDb(final Base base, final Compta module, final Account auxiliaryAccount, final TiersType tiersType, final ReconcileStatus status) {
		super(base, "Correspondance introuvable !");
		this.module = module;
		this.auxiliaryAccount = auxiliaryAccount;
		this.tiersType = tiersType;
		this.status = status;
		
		if(!auxiliaryAccount.isNone() && !auxiliaryAccount.isAuxiliary())
			throw new IllegalArgumentException("La gestion des correspondances ne concerne que les comptes tiers !");
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		AccountMetadata cptDm = AccountMetadata.create();
		TiersAccountMetadata tcptDm = TiersAccountMetadata.create();
		String statement = String.format("%s rec "
				+ "JOIN %s cpt ON cpt.%s = rec.%s "
				+ "left JOIN %s tcpt ON tcpt.%s=cpt.%s "
				+ "WHERE rec.%s=?",				 
				dm.domainName(), 
				cptDm.domainName(), cptDm.keyName(), dm.auxiliaryAccountIdKey(),
				tcptDm.domainName(), tcptDm.keyName(), cptDm.keyName(),
				dm.moduleIdKey());
		
		params.add(module.id());
		
		if(!auxiliaryAccount.isNone()){
			
			if(!auxiliaryAccount.isAuxiliary())
				throw new IllegalArgumentException("La gestion des correspondances ne concerne que les comptes tiers !");
			
			statement = String.format("%s AND cpt.%s=?", statement, cptDm.keyName());
			params.add(auxiliaryAccount.id());
		}
		
		if(!tiersType.isNone()){
			statement = String.format("%s AND tcpt.%s=?", statement, tcptDm.tiersTypeIdKey());
			params.add(tiersType.id());
		}
		
		if(status != ReconcileStatus.NONE){
			
			if(status == ReconcileStatus.PARTIAL)
				statement = String.format("%s AND rec.%s<>rec.%s", statement, dm.debitKey(), dm.creditKey());
			
			if(status == ReconcileStatus.COMPLETE)
				statement = String.format("%s AND rec.%s=rec.%s AND rec.%s=%s", statement, dm.debitKey(), dm.creditKey(), dm.lettratedKey(), false);
			
			if(status == ReconcileStatus.LETTRATED)
				statement = String.format("%s AND rec.%s=%s", statement, dm.lettratedKey(), true);
		}
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY rec.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("rec.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected Reconcile newOne(UUID id) {
		return new ReconcileDb(base, id, module);
	}

	@Override
	public Reconcile none() {
		return new ReconcileNone();
	}
	
	@Override
	public void delete (Reconcile item) throws IOException{
		if(contains(item)){
			
			if(item.isLettred())
				throw new IllegalArgumentException("Vous ne pouvez pas supprimer une correspondance lettrée !"); 
			
			for (Line line : item.allLines()) {
				line.unreconcile();
			}
			
			ds.delete(item.id());
		}
	}

	@Override
	public Reconciles of(Account auxiliaryAccount) throws IOException {						
		return new ReconcilesDb(base, module, auxiliaryAccount, tiersType, status);
	}

	@Override
	public Reconcile add() throws IOException {
		
		if(auxiliaryAccount.isNone())
			throw new IllegalArgumentException("Vous devez spécifier le compte tiers avant de poursuivre la correspondance !");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.debitKey(), 0);	
		params.put(dm.creditKey(), 0);
		params.put(dm.moduleIdKey(), module.id());
		params.put(dm.lettratedKey(), false);
		params.put(dm.auxiliaryAccountIdKey(), auxiliaryAccount.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return newOne(id);
	}

	@Override
	public Reconciles of(TiersType tiersType) throws IOException {
		return new ReconcilesDb(base, module, auxiliaryAccount, tiersType, status);
	}

	@Override
	public Reconciles of(ReconcileStatus status) throws IOException {
		return new ReconcilesDb(base, module, auxiliaryAccount, tiersType, status);
	}
}
