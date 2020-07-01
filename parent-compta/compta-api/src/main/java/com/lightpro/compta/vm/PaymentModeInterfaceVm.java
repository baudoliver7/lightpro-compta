package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.interfacage.sales.api.PaymentModeInterface;

public final class PaymentModeInterfaceVm {
	
	public final UUID id;
	public final String name;
	public final String type;
	public final String journalEncaissement;
	public final UUID journalEncaissementId;
	public final String reglement;
	public final UUID reglementId;
	public final String acompteOrAvance;
	public final UUID acompteOrAvanceId;
	public final String reglementDefinitif;
	public final UUID reglementDefinitifId;
	public final String provision;
	public final UUID provisionId;
	
	public PaymentModeInterfaceVm(){
		throw new UnsupportedOperationException("#PaymentModeSaleInterfaceVm()");
	}
		
	public PaymentModeInterfaceVm(PaymentModeInterface origin){
		try {
			this.id = origin.id();
			this.name = origin.paymentMode().name();
			this.type = origin.paymentMode().type().toString();
			this.journalEncaissement = origin.journalEncaissement().name();
	        this.journalEncaissementId = origin.journalEncaissement().id();
	        this.reglement = origin.reglement().name();	        
	        this.reglementId = origin.reglement().id();
	        this.acompteOrAvance = origin.acompteOrAvance().name();
	        this.acompteOrAvanceId = origin.acompteOrAvance().id();
	        this.reglementDefinitif = origin.reglementDefinitif().name();
	        this.reglementDefinitifId = origin.reglementDefinitif().id();
	        this.provision = origin.provision().name();
	        this.provisionId = origin.provision().id();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
