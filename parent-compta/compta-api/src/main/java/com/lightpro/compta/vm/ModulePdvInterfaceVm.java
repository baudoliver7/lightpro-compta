package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.compta.interfacage.sales.api.ModulePdvInterface;

public final class ModulePdvInterfaceVm {
	
	public final UUID id;
	public final String name;
	public final List<PdvPaymentModeInterfaceVm> paymentModes;
	
	public ModulePdvInterfaceVm(){
		throw new UnsupportedOperationException("#ModulePdvInterfaceVm()");
	}
		
	public ModulePdvInterfaceVm(ModulePdvInterface origin){
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.paymentModes = origin.paymentModes().all().stream().map(m -> new PdvPaymentModeInterfaceVm(m)).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
