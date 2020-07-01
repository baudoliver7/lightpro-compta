package com.compta.domains.api;

import com.compta.interfacage.sales.api.SalesInterface;
import com.securities.api.ModuleInterfacage;

public interface ComptaInterfacage extends ModuleInterfacage {
	SalesInterface salesInterface();
}
