package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.interfacage.sales.api.PdvPaymentModeInterface;

public final class PdvPaymentModeInterfaceVm {
	
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
	public final String paymentMode;
	public final UUID paymentModeId;
	public final String provision;
	public final UUID provisionId;
	
	public PdvPaymentModeInterfaceVm(){
		throw new UnsupportedOperationException("#PdvPaymentModeSaleInterfaceVm()");
	}
		
	public PdvPaymentModeInterfaceVm(PdvPaymentModeInterface origin){
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
	        this.paymentMode = origin.paymentMode().name();
	        this.paymentModeId = origin.paymentMode().id();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
