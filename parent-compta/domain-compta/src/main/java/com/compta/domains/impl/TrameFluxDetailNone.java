package com.compta.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.common.utilities.formular.Formular;
import com.compta.domains.api.Account;
import com.compta.domains.api.OperationSens;
import com.compta.domains.api.TrameFluxDetail;
import com.compta.domains.api.TrameFluxDetailSimilaryAccounts;
import com.compta.domains.api.TrameFlux;
import com.infrastructure.core.GuidKeyEntityNone;

public final class TrameFluxDetailNone extends GuidKeyEntityNone<TrameFluxDetail> implements TrameFluxDetail {

	@Override
	public Account generalAccount() throws IOException {
		return new AccountNone();
	}

	@Override
	public OperationSens sens() throws IOException {
		return OperationSens.NONE;
	}

	@Override
	public TrameFlux flux() throws IOException {
		return new TrameFluxNone();
	}

	@Override
	public boolean isAggregateAccount() throws IOException {
		return false;
	}

	@Override
	public Formular formular() throws IOException {
		return null;
	}

	@Override
	public Map<String, Double> buildParams(Double amountBase) throws IOException {
		return new HashMap<String, Double>();
	}

	@Override
	public TrameFluxDetailSimilaryAccounts similaryAccounts() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}
}
