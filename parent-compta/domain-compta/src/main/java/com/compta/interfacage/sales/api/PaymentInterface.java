package com.compta.interfacage.sales.api;

import java.io.IOException;

public interface PaymentInterface {
	PaymentModeInterfaces paymentModes() throws IOException;
}
