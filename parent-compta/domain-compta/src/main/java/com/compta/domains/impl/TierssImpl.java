package com.compta.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Tiers;
import com.compta.domains.api.Tierss;

public class TierssImpl implements Tierss {

	private transient Compta module;
	
	public TierssImpl(final Compta module){
		this.module = module;
	}
	
	@Override
	public Tiers build(UUID id) {
		try {
			return new TiersImpl(module.contacts().build(id), module);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public Tiers defaultTiers() throws IOException {
		return new TiersImpl(module.contacts().defaultPerson(), module);
	}
}
