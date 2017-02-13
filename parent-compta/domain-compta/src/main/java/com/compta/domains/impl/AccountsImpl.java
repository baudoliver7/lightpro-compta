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
import com.compta.domains.api.Account;
import com.compta.domains.api.AccountChart;
import com.compta.domains.api.AccountMetadata;
import com.compta.domains.api.AccountType;
import com.compta.domains.api.Accounts;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;

public class AccountsImpl implements Accounts {

	private transient final Base base;
	private final transient AccountMetadata dm;
	private final transient DomainsStore ds;
	private final transient AccountChart chart;
	
	public AccountsImpl(final Base base, final AccountChart chart){
		this.base = base;
		this.dm = AccountMetadata.create();
		this.ds = this.base.domainsStore(this.dm);	
		this.chart = chart;
	}
	
	@Override
	public List<Account> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<Account> find(int page, int pageSize, String filter) throws IOException {
		
		String statement = String.format("SELECT %s FROM %s "
				+ "WHERE (%s ILIKE ? OR %s ILIKE ?) AND %s=? "
				+ "ORDER BY %s ASC LIMIT ? OFFSET ?", 
				dm.keyName(), dm.domainName(), 
				dm.nameKey(), dm.codeKey(), dm.chartIdKey(),
				dm.codeKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(chart.id());
		
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
				dm.nameKey(), dm.codeKey(), dm.chartIdKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(chart.id());
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());
	}

	@Override
	public List<Account> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public Account build(UUID id) {
		return new AccountImpl(base, id);
	}

	@Override
	public boolean contains(Account item) {
		try {
			return item.isPresent() && item.chart().isEqual(chart);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Account get(UUID id) throws IOException {
		Account item = build(id);
		
		if(!contains(item))
			throw new NotFoundException("Le compte n'a pas été trouvé !");
		
		return item;
	}

	@Override
	public void delete(Account item) throws IOException {
		if(contains(item))
			ds.delete(item.id());		
	}

	@Override
	public Account add(String code, String name, AccountType type) throws IOException {
		
		if (StringUtils.isBlank(code) || code.length() != chart.codeDigits())
            throw new IllegalArgumentException(String.format("Invalid code : code must have %s digits !", chart.codeDigits()));
		
		if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.codeKey(), code);	
		params.put(dm.nameKey(), name);
		params.put(dm.typeIdKey(), type.id());
		params.put(dm.chartIdKey(), chart.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

}
