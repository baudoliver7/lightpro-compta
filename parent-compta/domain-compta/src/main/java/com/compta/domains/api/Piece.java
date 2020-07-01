package com.compta.domains.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.infrastructure.core.Nonable;
import com.securities.api.Module;

public interface Piece extends Nonable {
	UUID id();
	LocalDate date() throws IOException;
	PieceType type() throws IOException;
	PieceNature nature() throws IOException;
	String reference() throws IOException;
	String origin() throws IOException;
	String notes() throws IOException;
	PieceStatus status() throws IOException;
	LocalDate dateEcheance() throws IOException;
	Tiers tiers() throws IOException;
	Module moduleOrigin() throws IOException;
	Exercise exercise() throws IOException;
	boolean isInvoiceOrPayment() throws IOException;
	PieceArticles articles() throws IOException;
	
	void count() throws IOException;	
	void update(LocalDate date, String reference, String origin, String notes, LocalDate dateEcheance, Tiers tiers) throws IOException;
	void reconcile() throws IOException;
	void validate() throws IOException;
}
