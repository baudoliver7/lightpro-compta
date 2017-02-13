package com.compta.domains.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.AccountChart;
import com.compta.domains.api.AccountChartMetadata;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Journals;
import com.infrastructure.core.Horodate;
import com.infrastructure.datasource.Base;
import com.securities.api.Company;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.securities.impl.BasisModule;

public class ComptaImpl implements Compta {

	private final transient Base base;
	private final transient Module origin;
	
	public ComptaImpl(final Base base, final UUID id){
		this.base = base;
		this.origin = new BasisModule(base, id);
	}
	
	@Override
	public Company company() throws IOException {
		return origin.company();
	}

	@Override
	public String description() throws IOException {
		return origin.description();
	}

	@Override
	public void install() throws IOException {
		origin.install();
	}

	@Override
	public boolean isAvailable() {
		return origin.isAvailable();
	}

	@Override
	public boolean isInstalled() {
		return origin.isInstalled();
	}

	@Override
	public boolean isSubscribed() {
		return origin.isSubscribed();
	}

	@Override
	public String name() throws IOException {
		return origin.name();
	}

	@Override
	public int order() throws IOException {
		return origin.order();
	}

	@Override
	public String shortName() throws IOException {
		return origin.shortName();
	}

	@Override
	public ModuleType type() throws IOException {
		return origin.type();
	}

	@Override
	public void uninstall() throws IOException {
		origin.uninstall();
	}

	@Override
	public Horodate horodate() {
		return origin.horodate();
	}

	@Override
	public UUID id() {
		return origin.id();
	}

	@Override
	public boolean isEqual(Module item) {
		return origin.isEqual(item);
	}

	@Override
	public boolean isNotEqual(Module item) {
		return origin.isNotEqual(item);
	}

	@Override
	public boolean isPresent() {
		return origin.isPresent();
	}

	@Override
	public AccountChart chart() throws IOException {
		AccountChartMetadata accDm = AccountChartMetadata.create();
		
		String statement = String.format("SELECT %s FROM %s WHERE %s=?", accDm.keyName(), accDm.domainName(), accDm.moduleIdKey());		
		List<Object> results = base.executeQuery(statement, Arrays.asList(origin.id()));
		
		if(results.isEmpty()) {
			throw new IllegalArgumentException("Le plan comptable n'est pas configuré !");
		}else{
			return new AccountChartImpl(base, UUIDConvert.fromObject(results.get(0)));
		}		
	}

	@Override
	public Journals journals() throws IOException {
		return new JournalsImpl(base, this);
	}

	@Override
	public void activate(boolean active) throws IOException {
		origin.activate(active);
	}

	@Override
	public boolean isActive() {
		return origin.isActive();
	}
}
