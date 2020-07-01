package com.compta.domains.impl;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.AccountChart;
import com.compta.domains.api.AccountChartMetadata;
import com.compta.domains.api.AccountNature;
import com.compta.domains.api.AccountType;
import com.compta.domains.api.Accounts;
import com.compta.domains.api.Compta;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;

public final class AccountChartDb extends GuidKeyEntityDb<AccountChart, AccountChartMetadata> implements AccountChart {

	private final Compta module;
	
	public AccountChartDb(final Base base, final UUID id, final Compta module){
		super(base, id, "Plan comptable introuvable !");
		this.module = module;
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
		return new AccountsDb(base, module, this, new TiersNone(), new TiersTypeNone(), AccountNature.NONE, AccountType.NONE);
	}

	@Override
	public Compta module() throws IOException {
		return module;
	}

	@Override
	public String accountCode(String codeBase) throws IOException {
		return StringUtils.rightPad(codeBase, codeDigits(), "0");
	}
}
