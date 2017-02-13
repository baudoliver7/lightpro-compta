package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalMetadata;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.Journals;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;

public class JournalsImpl implements Journals {

	private transient final Base base;
	private final transient JournalMetadata dm;
	private final transient DomainsStore ds;
	private final transient Compta module;
	
	public JournalsImpl(final Base base, final Compta module){
		this.base = base;
		this.dm = JournalMetadata.create();
		this.ds = this.base.domainsStore(this.dm);	
		this.module = module;
	}
	
	@Override
	public List<Journal> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<Journal> find(int page, int pageSize, String filter) throws IOException {
		String statement = String.format("SELECT %s FROM %s "
				+ "WHERE (%s ILIKE ? OR %s ILIKE ?) AND %s=? "
				+ "ORDER BY %s ASC LIMIT ? OFFSET ?", 
				dm.keyName(), dm.domainName(), 
				dm.nameKey(), dm.codeKey(), dm.moduleIdKey(),
				dm.nameKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(pageSize > 0){
			params.add(pageSize);
			params.add((page - 1) * pageSize);
		}else{
			params.add(null);
			params.add(0);
		}
		
		return  ds.find(statement, params)
				  .stream()
				  .map(m -> build(UUIDConvert.fromObject(m)))
				  .collect(Collectors.toList());
	}

	@Override
	public int totalCount(String filter) throws IOException {
		
		String statement = String.format("SELECT COUNT(%s) FROM %s "
				+ "WHERE (%s ILIKE ? OR %s ILIKE ?) AND %s=? ",
				dm.keyName(), dm.domainName(), 
				dm.nameKey(), dm.codeKey(), dm.moduleIdKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(module.id());
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());
	}

	@Override
	public List<Journal> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public Journal build(UUID id) {
		return new JournalImpl(base, id);
	}

	@Override
	public boolean contains(Journal item) {
		try {
			return item.isPresent() && item.module().isEqual(module);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Journal get(UUID id) throws IOException {
		Journal item = build(id);
		
		if(!contains(item))
			throw new NotFoundException("Le journal n'a pas été trouvé !");
		
		return item;
	}

	@Override
	public void delete(Journal item) throws IOException {
		if(contains(item))
			ds.delete(item.id());		
	}

	@Override
	public Journal add(String code, String name, JournalType type) throws IOException {
		
		if (StringUtils.isBlank(code))
			throw new IllegalArgumentException("Invalid code : it can't be empty!");
		
		if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.codeKey(), code);	
		params.put(dm.nameKey(), name);
		params.put(dm.typeIdKey(), type.id());
		params.put(dm.moduleIdKey(), module.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}
}
