package com.compta.interfacage.sales.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PaymentModeInterfaces {
	List<PaymentModeInterface> all() throws IOException;
	PaymentModeInterface get(UUID id) throws IOException;
}
