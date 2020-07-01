package com.compta.interfacage.sales.impl;

import com.compta.domains.api.Compta;
import com.compta.interfacage.sales.api.ModulePdvInterfaces;
import com.compta.interfacage.sales.api.PdvInterface;
import com.infrastructure.datasource.Base;

public final class PdvInterfaceDb implements PdvInterface {

	private final Base base;
	private final Compta module;
	
	public PdvInterfaceDb(final Base base, final Compta module){
		this.base = base;
		this.module = module;
	}
	
	@Override
	public ModulePdvInterfaces modulePdvInterfaces() {
		return new ModulePdvInterfacesDb(base, module);
	}

}
