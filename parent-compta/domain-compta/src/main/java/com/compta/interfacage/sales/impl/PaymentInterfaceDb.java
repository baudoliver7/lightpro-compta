package com.compta.interfacage.sales.impl;

import java.io.IOException;

import com.compta.domains.api.Compta;
import com.compta.interfacage.sales.api.PaymentModeInterfaces;
import com.compta.interfacage.sales.api.PaymentInterface;
import com.infrastructure.datasource.Base;

public final class PaymentInterfaceDb implements PaymentInterface {

	private transient final Compta module;
	private transient final Base base;
	
	public PaymentInterfaceDb(Base base, Compta module){
		this.base = base;
		this.module = module;
	}
	
	@Override
	public PaymentModeInterfaces paymentModes() throws IOException {
		return new PaymentModeInterfacesDb(base, module);
	}

}
