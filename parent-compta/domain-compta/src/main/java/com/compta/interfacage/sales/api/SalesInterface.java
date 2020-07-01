package com.compta.interfacage.sales.api;

import java.io.IOException;

import com.securities.api.ModuleInterface;

public interface SalesInterface extends ModuleInterface {
	
	InvoiceInterface invoiceInterface() throws IOException;
	PaymentInterface paymentInterface() throws IOException;
	PdvInterface pdvInterface() throws IOException;
}
