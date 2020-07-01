package com.compta.interfacage.sales.impl;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.interfacage.sales.api.ModulePdvInterface;
import com.compta.interfacage.sales.api.ModulePdvInterfaceMetadata;
import com.compta.interfacage.sales.api.PdvPaymentModeInterfaces;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.securities.api.Company;
import com.securities.impl.CompanyDb;

public final class ModulePdvInterfaceDb extends GuidKeyEntityDb<ModulePdvInterface, ModulePdvInterfaceMetadata> implements ModulePdvInterface {

	private final transient Compta module;
	
	public ModulePdvInterfaceDb(final Base base, final UUID id, final Compta module){
		super(base, id, "Module de vente introuvable !");
		this.module = module;
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public Company company() throws IOException {
		UUID companyId = ds.get(dm.companyIdKey());
		return new CompanyDb(base, companyId);
	}

	@Override
	public PdvPaymentModeInterfaces paymentModes() throws IOException {
		return new PdvPaymentModeInterfacesDb(base, this, module);
	}
}
