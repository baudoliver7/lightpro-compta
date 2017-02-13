package com.compta.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalMetadata;
import com.compta.domains.api.JournalType;
import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;

public class JournalImpl implements Journal {

	private final transient Base base;
	private final transient JournalMetadata dm;
	private final transient UUID id;
	private final transient DomainStore ds;
	
	public JournalImpl(final Base base, final UUID id){
		this.base = base;
		this.dm = JournalMetadata.create();
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
	public boolean isEqual(Journal item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(Journal item) {
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
	public JournalType type() throws IOException {
		int typeId = ds.get(dm.typeIdKey());
		return JournalType.get(typeId);				
	}

	@Override
	public Compta module() throws IOException {
		UUID moduleId = ds.get(dm.moduleIdKey());
		return new ComptaImpl(base, moduleId);
	}

	@Override
	public void update(String code, String name, JournalType type) throws IOException {
		
		if (StringUtils.isBlank(code))
            throw new IllegalArgumentException("Invalid code : it can't be empty!");
		
		if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.codeKey(), code);	
		params.put(dm.nameKey(), name);
		params.put(dm.typeIdKey(), type.id());
		
		ds.set(params);	
	}
}
