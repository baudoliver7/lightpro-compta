package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.compta.interfacage.sales.api.PaymentInterface;

public final class PaymentInterfaceVm {
	
	public final List<PaymentModeInterfaceVm> paymentModes;
	
	public PaymentInterfaceVm(){
		throw new UnsupportedOperationException("#PaymentSaleInterfaceVm()");
	}
		
	public PaymentInterfaceVm(PaymentInterface origin){
		try {
			this.paymentModes = origin.paymentModes().all().stream().map(m -> new PaymentModeInterfaceVm(m)).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
