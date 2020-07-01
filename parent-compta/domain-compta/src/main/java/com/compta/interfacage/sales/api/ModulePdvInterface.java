package com.compta.interfacage.sales.api;

import java.io.IOException;
import java.util.UUID;

import com.securities.api.Company;

public interface ModulePdvInterface {
	UUID id();
	String name() throws IOException;
    Company company() throws IOException;
    
    PdvPaymentModeInterfaces paymentModes() throws IOException;
}
