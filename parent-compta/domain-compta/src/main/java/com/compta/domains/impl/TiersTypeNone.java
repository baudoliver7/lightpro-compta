package com.compta.domains.impl;

import java.io.IOException;

import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Lines;
import com.compta.domains.api.Reconciles;
import com.compta.domains.api.TiersCode;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.GuidKeyEntityNone;
import com.securities.api.Sequence;
import com.securities.impl.SequenceNone;

public final class TiersTypeNone extends GuidKeyEntityNone<TiersType> implements TiersType {
		
	@Override
	public TiersCode code() throws IOException {
		return TiersCode.OTHER;
	}

	@Override
	public String name() throws IOException {
		return "Type de tiers non défini";
	}

	@Override
	public Account generalAccount() throws IOException {
		return new AccountNone();
	}

	@Override
	public Compta module() throws IOException {
		return null;
	}

	@Override
	public Sequence sequence() throws IOException {
		return new SequenceNone();
	}

	@Override
	public void update(String name, Account generalAccount, Sequence sequence, Sequence sequenceLettrage) throws IOException {
		
	}

	@Override
	public Sequence sequenceLettrage() throws IOException {
		return new SequenceNone();
	}

	@Override
	public Reconciles reconciles() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : type de tiers inexistant !");
	}

	@Override
	public Lines lines() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : type de tiers inexistant !");
	}
}
