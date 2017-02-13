package com.lightpro.compta.rs;

import java.io.IOException;

import com.compta.domains.api.Compta;
import com.compta.domains.impl.ComptaImpl;
import com.securities.api.BaseRs;
import com.securities.api.Module;
import com.securities.api.ModuleType;

public abstract class ComptaBaseRs extends BaseRs {
	protected Compta compta() throws IOException {
		Module module = currentCompany().modules().get(ModuleType.COMPTA);
		return new ComptaImpl(base(), module.id());
	}
}
