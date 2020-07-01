package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.formular.Formular;
import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.Line;
import com.compta.domains.api.OperationSens;
import com.compta.domains.api.TrameFluxDetail;
import com.compta.domains.api.TrameFluxDetailMetadata;
import com.compta.domains.api.TrameFluxDetails;
import com.compta.domains.api.TrameFlux;
import com.compta.domains.api.TrameFluxMetadata;
import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;

public final class TrameFluxDetailsDb extends GuidKeyAdvancedQueryableDb<TrameFluxDetail, TrameFluxDetailMetadata> implements TrameFluxDetails {

	private transient final TrameFlux flux;
	private final Compta module;
	
	public TrameFluxDetailsDb(Base base, TrameFlux flux, Compta module) {
		super(base, "Détail de flux de trame introuvable !");
		
		this.flux = flux;
		this.module = module;
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		TrameFluxMetadata fluxDm = TrameFluxMetadata.create();
		String statement = String.format("%s trd "
				+ "JOIN %s flux ON flux.%s = trd.%s "
				+ "WHERE trd.%s=?",				 
				dm.domainName(), 
				fluxDm.domainName(), fluxDm.keyName(), dm.trameFluxIdKey(),
				dm.trameFluxIdKey());
		
		params.add(flux.id());
		
		String orderClause = String.format("ORDER BY trd.%s ASC", dm.orderKey());
		
		String keyResult = String.format("trd.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected TrameFluxDetail newOne(UUID id) {
		return new TrameFluxDetailDb(base, id, module);
	}

	@Override
	public TrameFluxDetail none() {
		return new TrameFluxDetailNone();
	}

	@Override
	public void deleteAll() throws IOException {
		for (TrameFluxDetail item : all()) {
			delete(item);
		}	
	}
	
	@Override
	public void delete(TrameFluxDetail item) throws IOException {
		if(contains(item)) {
			item.similaryAccounts().deleteAll();
			ds.delete(item.id()); 
		}
	}

	@Override
	public void validate() throws IOException {
		boolean hasCreditLine = false;
		boolean hasDebitLine = false;
		List<TrameFluxDetail> details = all();
		
		for (TrameFluxDetail detail : details) {
			
			if(detail.sens() == OperationSens.CREDIT)
				hasCreditLine = true;
			
			if(detail.sens() == OperationSens.DEBIT)
				hasDebitLine = true;			
		}
		
		if(!details.isEmpty() && !(hasDebitLine && hasCreditLine))
			throw new IllegalArgumentException("Le flux doit comporter des lignes au débit et au crédit !"); 
	}

	@Override
	public List<Line> generateLines(Journal journal, Map<String, Double> params) throws IOException {
		return new LinesTemplate(flux, journal, params).all();
	}

	@Override
	public TrameFluxDetail add(Account generalAccount, OperationSens sens, boolean isAggregateAccount, String expression) throws IOException {
		TrameFluxDetailDb.validate(generalAccount, sens);
		
		Map<String, Object> params = new HashMap<String, Object>();	
		params.put(dm.generalAccountIdKey(), generalAccount.id());
		params.put(dm.sensIdKey(), sens.id());
		params.put(dm.trameFluxIdKey(), flux.id());
		params.put(dm.isAggregateAccountKey(), isAggregateAccount);
		
		Formular formular = module.company().currency().calculator().withExpression(expression);
		formular.validate();
		
		params.put(dm.formular(), formular.expression());
		
		int order = all().size() + 1;
		params.put(dm.orderKey(), order);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	public TrameFluxDetail add(Account generalAccount, OperationSens sens, String formular) throws IOException {
		return add(generalAccount, sens, formular);
	}
}
