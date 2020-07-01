package com.compta.interfacage.sales.api;

import java.io.IOException;

import com.infrastructure.core.GuidKeyQueryable;
import com.securities.api.PaymentMode;

public interface PdvPaymentModeInterfaces extends GuidKeyQueryable<PdvPaymentModeInterface> {
	boolean contains(PaymentMode mode) throws IOException;
	PdvPaymentModeInterface get(PaymentMode mode) throws IOException;
}
