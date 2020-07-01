package com.compta.interfacage.sales.api;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.infrastructure.core.Nonable;
import com.securities.api.PaymentMode;

public interface PdvPaymentModeInterface extends Nonable {	
	UUID id();
	PaymentMode paymentMode() throws IOException;
	Journal journalEncaissement() throws IOException;
	PieceType reglement() throws IOException;
	Trame acompteOrAvance() throws IOException;
	Trame reglementDefinitif() throws IOException;
	Trame provision() throws IOException;
	ModulePdvInterface pdv() throws IOException;
	
	void update(Journal journalEncaissement, PieceType reglement, Trame acompteOrAvance, Trame reglementDefinitif, Trame provision) throws IOException;
}
