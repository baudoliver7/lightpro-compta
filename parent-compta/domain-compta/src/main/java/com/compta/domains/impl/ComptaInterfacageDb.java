package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.compta.domains.api.Compta;
import com.compta.domains.api.ComptaInterfacage;
import com.compta.interfacage.sales.api.SalesInterface;
import com.compta.interfacage.sales.impl.SalesInterfaceDb;
import com.infrastructure.datasource.Base;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.securities.api.Modules;

public final class ComptaInterfacageDb implements ComptaInterfacage {

	private final Base base;
	private final Compta compta;
	
	public ComptaInterfacageDb(final Base base, final Compta compta){
		this.base = base;
		this.compta = compta;
	}
	
	@Override
	public List<Module> modulesAvailable() throws IOException {
		
		List<Module> modules = new ArrayList<Module>();
		
		Modules modulesInstalled = compta.company().modulesInstalled();
		
		if(modulesInstalled.contains(ModuleType.SALES))
			modules.add(modulesInstalled.get(ModuleType.SALES));
		
		return modules;
	}

	@Override
	public SalesInterface salesInterface() {
		return new SalesInterfaceDb(base, compta);
	}
	
}
