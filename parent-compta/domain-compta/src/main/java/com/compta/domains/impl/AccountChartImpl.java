package com.compta.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.AccountChart;
import com.compta.domains.api.AccountChartMetadata;
import com.compta.domains.api.Accounts;
import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;

public class AccountChartImpl implements AccountChart {

	private final transient Base base;
	private final transient AccountChartMetadata dm;
	private final transient UUID id;
	private final transient DomainStore ds;
	
	public AccountChartImpl(final Base base, final UUID id){
		this.base = base;
		this.dm = AccountChartMetadata.create();
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
	public boolean isEqual(AccountChart item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(AccountChart item) {
		return !isEqual(item);
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public int codeDigits() throws IOException {
		return ds.get(dm.codeDigitsKey());
	}

	@Override
	public Accounts accounts() throws IOException {
		return new AccountsImpl(base, this);
	}

}
