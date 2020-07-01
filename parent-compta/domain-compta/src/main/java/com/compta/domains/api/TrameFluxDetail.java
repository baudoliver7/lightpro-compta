package com.compta.domains.api;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.common.utilities.formular.Formular;
import com.infrastructure.core.Nonable;

public interface TrameFluxDetail extends Nonable {
	UUID id();
	Account generalAccount() throws IOException;
	OperationSens sens() throws IOException;
	TrameFlux flux() throws IOException;
	boolean isAggregateAccount() throws IOException;
	Formular formular() throws IOException;
	Map<String, Double> buildParams(Double amountBase) throws IOException;
	TrameFluxDetailSimilaryAccounts similaryAccounts() throws IOException;
}
