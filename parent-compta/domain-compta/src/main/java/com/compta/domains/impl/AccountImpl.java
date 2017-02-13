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
import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;

public class AccountImpl implements Account {

	private final transient Base base;
	private final transient AccountMetadata dm;
	private final transient UUID id;
	private final transient DomainStore ds;
	
	public AccountImpl(final Base base, final UUID id){
		this.base = base;
		this.dm = AccountMetadata.create();
		this.ds = this.base.domainsStore(this.dm).createDs(id);	
		this.id = id;
	}
	
	@Override
	public Horodate horodate() {
		return new HorodateImpl(ds);
	}

	@Override
	public UUID id() {
		return id;
	}

	@Override
	public boolean isPresent() {
		return base.domainsStore(dm).exists(id);
	}

	@Override
	public boolean isEqual(Account item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(Account item) {
		return !isEqual(item);
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
		return new AccountChartImpl(base, chartId);
	}

	@Override
	public void update(String code, String name, AccountType type) throws IOException {
		
		if (StringUtils.isBlank(code) || code.length() != chart().codeDigits())
			throw new IllegalArgumentException(String.format("Invalid code : code must have %s digits !", chart().codeDigits()));
		
		if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.codeKey(), code);	
		params.put(dm.nameKey(), name);
		params.put(dm.typeIdKey(), type.id());
		
		ds.set(params);	
	}
}
