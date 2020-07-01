package com.compta.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface Reconcile extends Nonable {
	UUID id();
	Account auxiliaryAccount() throws IOException;
	List<Line> invoiceLines() throws IOException;
	List<Line> paymentLines() throws IOException;
	List<Line> allLines() throws IOException;
	double debit() throws IOException;
	double credit() throws IOException;
	boolean isLettred();
	
	void add(Line line) throws IOException;
	void remove(Line line) throws IOException;
	void removeAll() throws IOException;
	void delettrer() throws IOException;
	void lettrer() throws IOException;
	void updateAmounts() throws IOException;
	
	Compta module() throws IOException;
}