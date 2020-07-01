package com.compta.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Account;
import com.compta.domains.api.AccountChart;
import com.compta.domains.api.AccountMetadata;
import com.compta.domains.api.AccountType;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Lines;
import com.compta.domains.api.TiersAccountMetadata;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;

public final class AccountDb extends GuidKeyEntityDb<Account, AccountMetadata> implements Account {

	private final Compta module;
	
	public AccountDb(final Base base, final UUID id, final Compta module){
		super(base, id, "Compte introuvable !");
		this.module = module;
	}

	@Override
	public String code() throws IOException {
		return ds.get(dm.codeKey());
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public AccountType type() throws IOException {
		int typeId = ds.get(dm.typeIdKey());
		return AccountType.get(typeId);
	}

	@Override
	public AccountChart chart() throws IOException {
		UUID chartId = ds.get(dm.chartIdKey());
		return new AccountChartDb(base, chartId, module);
	}

	@Override
	public void update(String code, String name, AccountType type, boolean refuseCreditBalance, boolean refuseDebitBalance) throws IOException {
		
		if (!isAuxiliary() && (StringUtils.isBlank(code) || code.length() != chart().codeDigits()))
			throw new IllegalArgumentException(String.format("Invalid code : code must have %s digits !", chart().codeDigits()));
		
		if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.codeKey(), code);	
		params.put(dm.nameKey(), name);
		params.put(dm.typeIdKey(), type.id());
		params.put(dm.refuseCreditBalanceKey(), refuseCreditBalance);
		params.put(dm.refuseDebitBalanceKey(), refuseDebitBalance);
		
		ds.set(params);	
	}

	@Override
	public String fullName() throws IOException {
		return String.format("%s - %s", code(), name());
	}

	@Override
	public boolean isAuxiliary() {
		try {
			return ds.get(dm.isAuxiliaryKey());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TiersType tiersType() throws IOException {
		if(isAuxiliary())
		{
			TiersAccountMetadata dm = TiersAccountMetadata.create();
			DomainStore ds = base.domainsStore(dm).createDs(id);
			UUID typeId = ds.get(dm.tiersTypeIdKey());
			return chart().module().tiersTypes().get(typeId); 
		}else{
			return new TiersTypeNone();
		}
	}

	@Override
	public Lines lines() throws IOException {
		
		if(isAuxiliary())
			return chart().module().lines().ofAuxiliaryAccount(this);
		else
			return chart().module().lines().ofGeneralAccount(this);
	}

	@Override
	public boolean refuseCreditBalance() throws IOException {
		return ds.get(dm.refuseCreditBalanceKey());
	}

	@Override
	public boolean refuseDebitBalance() throws IOException {
		return ds.get(dm.refuseDebitBalanceKey());
	}
}
