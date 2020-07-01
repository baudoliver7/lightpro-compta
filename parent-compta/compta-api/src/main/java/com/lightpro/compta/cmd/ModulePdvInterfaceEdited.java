package com.lightpro.compta.cmd;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ModulePdvInterfaceEdited {

	private final UUID id;
	private final List<PdvPaymentModeSaleInterfaceEdited> paymentModes;
	
	public ModulePdvInterfaceEdited(){
		throw new UnsupportedOperationException("#ModulePdvInterfaceEdited()");
	}
	
	@JsonCreator
	public ModulePdvInterfaceEdited(@JsonProperty("id") final UUID id,
			@JsonProperty("paymentModes") final List<PdvPaymentModeSaleInterfaceEdited> paymentModes){
		
		this.id = id;
		this.paymentModes = paymentModes;
	}
	
	public UUID id(){
		return id;
	}
	
	public List<PdvPaymentModeSaleInterfaceEdited> paymentModes(){
		return paymentModes;
	}
}
