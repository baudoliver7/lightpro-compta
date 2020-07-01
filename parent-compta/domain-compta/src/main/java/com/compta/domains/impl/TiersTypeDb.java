package com.compta.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Lines;
import com.compta.domains.api.Reconciles;
import com.compta.domains.api.TiersCode;
import com.compta.domains.api.TiersType;
import com.compta.domains.api.TiersTypeMetadata;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.securities.api.Sequence;

public final class TiersTypeDb extends GuidKeyEntityDb<TiersType, TiersTypeMetadata> implements TiersType {
	
	private final Compta module;
	
	public TiersTypeDb(final Base base, final UUID id, final Compta module){
		super(base, id, "Type de tiers introuvable !");
		this.module = module;
	}
	
	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public Account generalAccount() throws IOException {
		UUID accountId = ds.get(dm.generalAccountIdIdKey());
		return new AccountDb(base, accountId, module);
	}

	@Override
	public Compta module() throws IOException {
		return module;
	}

	@Override
	public void update(String name, Account generalAccount, Sequence sequence, Sequence sequenceLettrage) throws IOException {
		validate(name, generalAccount, sequence, sequenceLettrage);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);	
		params.put(dm.generalAccountIdIdKey(), generalAccount.id());
		params.put(dm.sequenceIdKey(), sequence.id());
		params.put(dm.sequenceLettrageIdKey(), sequenceLettrage.id());
		
		ds.set(params);	
	}
	
	public static void validate(String name, Account generalAccount, Sequence sequence, Sequence sequenceLettrage){
		
		if(StringUtils.isBlank(name))
			throw new IllegalArgumentException("Le libellé doit être renseigné !");
		
		if(generalAccount.isNone())
			throw new IllegalArgumentException("Vous devez spécifier un compte général !");
		
		if(sequence.isNone())
			throw new IllegalArgumentException("Séquence des comptes : vous devez spécifier une séquence !");
		
		if(sequenceLettrage.isNone())
			throw new IllegalArgumentException("Séquence du lettrage : vous devez spécifier une séquence !");
	}
	
	@Override
	public TiersCode code() throws IOException {
		int codeId = ds.get(dm.codeIdKey());
		return TiersCode.get(codeId);
	}

	@Override
	public Sequence sequence() throws IOException {
		UUID sequenceId = ds.get(dm.sequenceIdKey());
		return module().sequences().get(sequenceId);
	}

	@Override
	public Sequence sequenceLettrage() throws IOException {
		UUID sequenceId = ds.get(dm.sequenceLettrageIdKey());
		return module().sequences().build(sequenceId);
	}

	@Override
	public Reconciles reconciles() throws IOException {
		return module().reconciles().of(this);
	}

	@Override
	public Lines lines() throws IOException {
		return module().lines().of(this);
	}
}
