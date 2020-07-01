package com.compta.interfacage.sales.impl;

import java.io.IOException;

import com.compta.interfacage.sales.api.ModulePdvInterface;
import com.compta.interfacage.sales.api.PdvPaymentModeInterfaces;
import com.infrastructure.core.GuidKeyEntityNone;
import com.securities.api.Company;
import com.securities.impl.CompanyNone;

public final class ModulePdvInterfaceNone extends GuidKeyEntityNone<ModulePdvInterface> implements ModulePdvInterface {

	@Override
	public String name() throws IOException {
		return "Aucun module de vente";
	}

	@Override
	public Company company() throws IOException {
		return new CompanyNone();
	}

	@Override
	public PdvPaymentModeInterfaces paymentModes() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}
}
