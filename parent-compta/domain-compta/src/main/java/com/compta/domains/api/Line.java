package com.compta.domains.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface Line extends Nonable {
	UUID id();
	int order() throws IOException;
	Piece piece() throws IOException;
	Flux flux() throws IOException;
	LocalDate dateEcheance() throws IOException;
	Account generalAccount() throws IOException;
	Account auxiliaryAccount() throws IOException;
	Tiers tiers() throws IOException;
	double debit() throws IOException;
	double credit() throws IOException;
	double balance() throws IOException;
	OperationSens sens() throws IOException;
	String lettrage() throws IOException;
	Reconcile reconcile() throws IOException;
	boolean isNull() throws IOException;
	
	void reconcile(Reconcile reconcile) throws IOException;
	void unreconcile() throws IOException;
	
	void lettrer(String lettrage) throws IOException;
	void delettrer() throws IOException;
}
