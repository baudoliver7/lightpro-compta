package com.compta.domains.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.TrameFluxDetail;
import com.compta.domains.api.TrameFluxDetailSimilaryAccountMetadata;
import com.compta.domains.api.TrameFluxDetailSimilaryAccounts;
import com.infrastructure.core.GuidKeyQueryableDb;
import com.infrastructure.datasource.Base;

public final class TrameFluxDetailSimilaryAccountsDb extends GuidKeyQueryableDb<Account, TrameFluxDetailSimilaryAccountMetadata> implements TrameFluxDetailSimilaryAccounts {

	private final TrameFluxDetail detail;
	private final Compta module;
	
	public TrameFluxDetailSimilaryAccountsDb(final Base base, final TrameFluxDetail detail, final Compta module) {
		super(base, "Compte similaire introuvable !");
		this.detail = detail;
		this.module = module;
		
		if(detail.isNone())
			throw new IllegalArgumentException("Vous devez définir la ligne modèle associée aux comptes similaires !"); 
	}

	@Override
	public void add(Account account) throws IOException {
		
		if(account.isNone())
			throw new IllegalArgumentException("Vous devez définir un compte !");
		
		if(contains(account))
			return;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.accountIdKey(), account.id());
		params.put(dm.detailIdKey(), detail.id());
		
		int order = all().size() + 1;
		params.put(dm.orderKey(), order);
		
		ds.set(UUID.randomUUID(), params);		
	}

	@Override
	public void deleteAll() throws IOException {
		for (Account account : all()) {
			delete(account);
		}
	}
	
	@Override
	public void delete(Account account) throws IOException {
		if(contains(account)){
			String statement = String.format("SELECT %s FROM %s WHERE %s=? AND %s=?", dm.keyName(), dm.domainName(), dm.detailIdKey(), dm.accountIdKey());
			List<Object> results = base.executeQuery(statement, Arrays.asList(detail.id(), account.id()));
			UUID id = UUIDConvert.fromObject(results.get(0));
			ds.delete(id);
		}
	}

	@Override
	public List<Account> all() throws IOException {		
		String statement = String.format("SELECT %s FROM %s WHERE %s=?", dm.accountIdKey(), dm.domainName(), dm.detailIdKey());
		List<Object> results = base.executeQuery(statement, Arrays.asList(detail.id()));
		
		return results.stream()
				      .map(m -> newOne(UUIDConvert.fromObject(m)))
				      .collect(Collectors.toList());		
	}

	@Override
	protected Account newOne(UUID id) {
		return new AccountDb(base, id, module);
	}

	@Override
	public Account none() {
		return new AccountNone();
	}
}
