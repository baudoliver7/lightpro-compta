package com.compta.domains.impl;

import java.io.IOException;

import com.compta.domains.api.Accounts;
import com.compta.domains.api.Tiers;
import com.infrastructure.core.GuidKeyEntityNone;
import com.securities.api.Company;
import com.securities.api.Contact;
import com.securities.api.ContactNature;
import com.securities.impl.ContactNone;

public final class TiersNone extends GuidKeyEntityNone<Tiers> implements Tiers {

	private transient final Contact origin;
	
	public TiersNone(){
		this.origin = new ContactNone();
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
	public Accounts accounts() throws IOException {
		return null;
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
	public void updateAddresses(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5,
			String arg6) throws IOException {
		
	}

	@Override
	public String webSite() throws IOException {
		return origin.webSite();
	}
}
