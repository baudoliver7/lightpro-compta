package com.compta.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Accounts;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Tiers;
import com.infrastructure.core.GuidKeyEntityBase;
import com.securities.api.Company;
import com.securities.api.Contact;
import com.securities.api.ContactNature;

public final class TiersImpl extends GuidKeyEntityBase<Tiers> implements Tiers {

	private transient final Contact origin;
	private transient final Compta module;
	
	public TiersImpl(Contact origin, Compta module){
		super(origin.id());
		this.origin = origin;
		this.module = module;
	}

	@Override
	public Company company() throws IOException {
		return origin.company();
	}

	@Override
	public String photo() throws IOException {
		return origin.photo();
	}

	@Override
	public UUID id() {
		return origin.id();
	}

	@Override
	public Accounts accounts() throws IOException {
		return module.chart().accounts().of(this);
	}

	@Override
	public String fax() throws IOException {
		return origin.fax();
	}

	@Override
	public String locationAddress() throws IOException {
		return origin.locationAddress();
	}

	@Override
	public String mail() throws IOException {
		return origin.mail();
	}

	@Override
	public String mobile() throws IOException {
		return origin.mobile();
	}

	@Override
	public String name() throws IOException {
		return origin.name();
	}

	@Override
	public ContactNature nature() throws IOException {
		return origin.nature();
	}

	@Override
	public String phone() throws IOException {
		return origin.phone();
	}

	@Override
	public String poBox() throws IOException {
		return origin.poBox();
	}

	@Override
	public void updateAddresses(String locationAddress, String phone, String mobile, String fax, String mail, String poBox, String webSite) throws IOException {
		origin.updateAddresses(locationAddress, phone, mobile, fax, mail, poBox, webSite);
	}

	@Override
	public String webSite() throws IOException {
		return origin.webSite();
	}
}
