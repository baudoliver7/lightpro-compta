package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Account;
import com.compta.domains.api.AccountChart;
import com.compta.domains.api.AccountMetadata;
import com.compta.domains.api.AccountNature;
import com.compta.domains.api.AccountType;
import com.compta.domains.api.Accounts;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Tiers;
import com.compta.domains.api.TiersAccountMetadata;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.infrastructure.datasource.QueryBuilder;

public final class AccountsDb extends GuidKeyAdvancedQueryableDb<Account, AccountMetadata> implements Accounts {

	private final transient AccountChart chart;
	private final transient Tiers tiers;
	private final transient TiersType tiersType;
	private final transient AccountNature nature;
	private final transient AccountType accountType;
	private final Compta module;
	
	public AccountsDb(final Base base, final Compta module, final AccountChart chart, final Tiers tiers, final TiersType tiersType, final AccountNature nature, final AccountType accountType){
		super(base, "Compte introuvable !");	
		this.chart = chart;
		this.tiers = tiers;
		this.tiersType = tiersType;
		this.nature = nature;
		this.accountType = accountType;
		this.module = module;
	}
	
	@Override
	public List<Account> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
	
		TiersAccountMetadata tcptDm = TiersAccountMetadata.create();
		String statement = String.format("%s cpt "
				+ "LEFT JOIN %s tcpt ON tcpt.%s = cpt.%s "
				+ "WHERE (cpt.%s LIKE ? OR cpt.%s ILIKE ?) AND cpt.%s=?",		 
				dm.domainName(), 
				tcptDm.domainName(), tcptDm.keyName(), dm.keyName(),
				dm.codeKey(), dm.nameKey(), dm.chartIdKey());
		
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(chart.id());
		
		if(tiers.id() != null){
			statement = String.format("%s AND tcpt.%s=?", statement, tcptDm.tiersIdKey());
			params.add(tiers.id());
		}
		
		if(tiersType.id() != null){
			statement = String.format("%s AND tcpt.%s=?", statement, tcptDm.tiersTypeIdKey());
			params.add(tiersType.id());
		}
		
		if(accountType != AccountType.NONE){
			statement = String.format("%s AND cpt.%s=?", statement, dm.typeIdKey());
			params.add(accountType.id());
		}
		
		String orderClause = String.format("ORDER BY cpt.%s ASC", dm.codeKey());
		
		String keyResult = String.format("cpt.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	public List<Account> find(int page, int pageSize, String filter) throws IOException {
		
		return  buildQuery(filter).find(page, pageSize)
				  .stream()
				  .map(m -> build(UUIDConvert.fromObject(m)))
				  .collect(Collectors.toList());
	}

	@Override
	public Account add(String code, String name, AccountType type, boolean refuseCreditBalance, boolean refuseDebitBalance) throws IOException {
		
		if (code.length() != chart.codeDigits())
            throw new IllegalArgumentException(String.format("Code invalide : le code d'un compte général doit être sur %s digits !", chart.codeDigits()));
		
		if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
		
		if(!find(code).isEmpty())
			throw new IllegalArgumentException(String.format("Le code %s est déjà utilisé !", code));
			
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.codeKey(), code);	
		params.put(dm.nameKey(), name);
		params.put(dm.typeIdKey(), type.id());
		params.put(dm.chartIdKey(), chart.id());
		params.put(dm.isAuxiliaryKey(), false);
		params.put(dm.refuseCreditBalanceKey(), refuseCreditBalance);
		params.put(dm.refuseDebitBalanceKey(), refuseDebitBalance);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	public Account get(String code) throws IOException {
		List<Account> results = find(code);
		
		if(results.isEmpty())
			throw new IllegalArgumentException("Le compte n'a pas été trouvé !");
		
		if(results.size() > 1)
			throw new IllegalArgumentException(String.format("Plusieurs comptes correspondant au code %s ont été trouvés !", code));
		
		return results.get(0);
	}

	@Override
	public Account addAuxiliaryAccount(String code, String name, AccountType type, TiersType tiersType) throws IOException {
		
		if(tiers.id() == null)
			throw new IllegalArgumentException("Vous devez fournir le tiers pour lequel le compte doit être créé !");
		
		if(tiersType.id() == null)
			throw new IllegalArgumentException("Vous devez spécifier le type de compte tiers que vous voulez créé !");
		
		if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Le libellé du compte doit être renseigné !");
		
		if(!find(code).isEmpty())
			throw new IllegalArgumentException(String.format("Le code %s est déjà utilisé !", code));
			
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.codeKey(), code);	
		params.put(dm.nameKey(), name);
		params.put(dm.typeIdKey(), type.id());
		params.put(dm.chartIdKey(), chart.id());
		params.put(dm.isAuxiliaryKey(), true);
		params.put(dm.refuseCreditBalanceKey(), false);
		params.put(dm.refuseDebitBalanceKey(), false);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		TiersAccountMetadata tcptDm = TiersAccountMetadata.create();
		DomainsStore tcptDs = base.domainsStore(tcptDm);
		
		params = new HashMap<String, Object>();
		params.put(tcptDm.tiersIdKey(), tiers.id());
		params.put(tcptDm.tiersTypeIdKey(), tiersType.id());
		params.put(tcptDm.generalAccountIdKey(), tiersType.generalAccount().id());
		
		tcptDs.set(id, params);
		
		return build(id);
	}

	@Override
	public Account getAuxiliaryAccountOrDefault(TiersType type) throws IOException {
		
		if(tiers.id() == null)
			throw new IllegalArgumentException("Vous devez fournir le tiers pour lequel vous rechercher un compte !");
		
		Account auxiliaryAccount = build(null);
		
		for (Account account : all()) {
			if(account.tiersType().equals(type))
			{
				auxiliaryAccount = account;
				break;
			}
		}
		
		return auxiliaryAccount;
	}

	@Override
	public Accounts of(TiersType type) throws IOException {
		return new AccountsDb(base, module, chart, tiers, type, nature, accountType);
	}

	@Override
	public Accounts of(AccountNature nature) throws IOException {
		return new AccountsDb(base, module, chart, tiers, tiersType, nature, accountType);
	}

	@Override
	public Accounts of(Tiers tiers) throws IOException {
		return new AccountsDb(base, module, chart, tiers, tiersType, nature, accountType);
	}

	@Override
	public Accounts of(AccountType type) throws IOException {
		return new AccountsDb(base, module, chart, tiers, tiersType, nature, type);
	}

	@Override
	protected Account newOne(UUID id) {
		return new AccountDb(base, id, module);
	}

	@Override
	public Account none() {
		return new AccountNone();
	}
}
