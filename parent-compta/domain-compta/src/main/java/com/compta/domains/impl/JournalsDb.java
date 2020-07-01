package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalMetadata;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.Journals;
import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;

public final class JournalsDb extends GuidKeyAdvancedQueryableDb<Journal, JournalMetadata> implements Journals {

	private final transient Compta module;
	private final transient JournalType journalType;
	
	public JournalsDb(final Base base, final Compta module, JournalType journalType){
		super(base, "Journal introuvable !");
		this.module = module;
		this.journalType = journalType;
	}
	
	@Override
	public List<Journal> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		String statement = String.format("%s jrnl "
				+ "WHERE jrnl.%s=?",				 
				dm.domainName(), 
				dm.moduleIdKey());
		
		params.add(module.id());
		
		if(journalType != JournalType.NONE){
			statement = String.format("%s AND jrnl.%s=?", statement, dm.typeIdKey());
			params.add(journalType.id());
		}
	
		String orderClause = String.format("ORDER BY jrnl.%s DESC", dm.nameKey());
				
		String keyResult = String.format("jrnl.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	public Journal add(String code, String name, JournalType type, Account account, boolean validateAccount) throws IOException {
		
		JournalDb.validate(code, name, type, account);		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.codeKey(), code);	
		params.put(dm.nameKey(), name);
		params.put(dm.typeIdKey(), type.id());
		params.put(dm.moduleIdKey(), module.id());
		params.put(dm.accountIdKey(), account.id());
		params.put(dm.viewOnDashboardKey(), false);
		
		if(account.id() == null)
			params.put(dm.validateAccountKey(), false);
		else
			params.put(dm.validateAccountKey(), validateAccount);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return newOne(id);
	}

	@Override
	public Journal none() {
		return new JournalNone();
	}

	@Override
	public Journals of(JournalType journalType) throws IOException {
		return new JournalsDb(base, module, journalType);
	}

	@Override
	protected Journal newOne(UUID id) {
		return new JournalDb(base, id, module);
	}
}
