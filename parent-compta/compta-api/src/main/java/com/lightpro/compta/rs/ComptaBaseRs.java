package com.lightpro.compta.rs;

import java.io.IOException;

import com.compta.domains.api.Compta;
import com.compta.domains.impl.ComptaDb;
import com.securities.api.BaseRs;
import com.securities.api.Module;
import com.securities.api.ModuleType;

public abstract class ComptaBaseRs extends BaseRs {

	public ComptaBaseRs() {
		super(ModuleType.COMPTA);		
	}

	protected Compta compta() throws IOException {
		return compta(currentModule);
	}
	
	protected Compta compta(Module module) throws IOException {		
		return new ComptaDb(base, module);
	}
}
