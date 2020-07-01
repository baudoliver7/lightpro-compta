package com.lightpro.compta.cmd;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PaymentSaleInterfaceEdited {

	private final List<PaymentModeSaleInterfaceEdited> paymentModes;
	
	public PaymentSaleInterfaceEdited(){
		throw new UnsupportedOperationException("#PaymentSaleInterfaceEdited()");
	}
	
	@JsonCreator
	public PaymentSaleInterfaceEdited(@JsonProperty("paymentModes") final List<PaymentModeSaleInterfaceEdited> paymentModes){
		
		this.paymentModes = paymentModes;
	}
	
	public List<PaymentModeSaleInterfaceEdited> paymentModes(){
		return paymentModes;
	}
}
