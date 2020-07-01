package com.compta.domains.impl;

import java.io.IOException;

import com.compta.domains.api.AccountChart;
import com.compta.domains.api.Accounts;
import com.compta.domains.api.Compta;
import com.infrastructure.core.GuidKeyEntityNone;

public final class ChartNone extends GuidKeyEntityNone<AccountChart> implements AccountChart {

	@Override
	public String name() throws IOException {
		return "Aucun plan comptable";
	}

	@Override
	public int codeDigits() throws IOException {
		return 0;
	}

	@Override
	public Accounts accounts() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Compta module() throws IOException {
		return null;
	}

	@Override
	public String accountCode(String codeBase) throws IOException {
		return null;
	}
}
