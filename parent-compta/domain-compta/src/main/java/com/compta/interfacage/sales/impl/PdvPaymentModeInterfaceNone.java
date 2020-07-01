package com.compta.interfacage.sales.impl;

import java.io.IOException;

import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.impl.JournalNone;
import com.compta.domains.impl.PieceTypeNone;
import com.compta.domains.impl.TrameNone;
import com.compta.interfacage.sales.api.ModulePdvInterface;
import com.compta.interfacage.sales.api.PdvPaymentModeInterface;
import com.infrastructure.core.GuidKeyEntityNone;
import com.securities.api.PaymentMode;
import com.securities.impl.PaymentModeNone;

public final class PdvPaymentModeInterfaceNone extends GuidKeyEntityNone<PdvPaymentModeInterface> implements PdvPaymentModeInterface {

	@Override
	public PaymentMode paymentMode() throws IOException {
		return new PaymentModeNone();
	}

	@Override
	public Journal journalEncaissement() throws IOException {
		return new JournalNone();
	}

	@Override
	public PieceType reglement() throws IOException {
		return new PieceTypeNone();
	}

	@Override
	public Trame acompteOrAvance() throws IOException {
		return new TrameNone();
	}

	@Override
	public Trame reglementDefinitif() throws IOException {
		return new TrameNone();
	}

	@Override
	public ModulePdvInterface pdv() throws IOException {
		return new ModulePdvInterfaceNone();
	}

	@Override
	public void update(Journal journalEncaissement, PieceType reglement, Trame acompteOrAvance, Trame reglementDefinitif, Trame provision) throws IOException {
	
	}

	@Override
	public Trame provision() throws IOException {
		return new TrameNone();
	}
}
