package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;

import com.compta.domains.api.Exercise;
import com.compta.domains.api.Piece;
import com.compta.domains.api.PieceArticles;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.PieceStatus;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Tiers;
import com.infrastructure.core.GuidKeyEntityNone;
import com.securities.api.Module;
import com.securities.impl.ModuleNone;

public final class PieceNone extends GuidKeyEntityNone<Piece> implements Piece {

	@Override
	public LocalDate date() throws IOException {
		return null;
	}

	@Override
	public PieceType type() throws IOException {
		return new PieceTypeNone();
	}

	@Override
	public String reference() throws IOException {
		return null;
	}

	@Override
	public String notes() throws IOException {
		return null;
	}

	@Override
	public PieceStatus status() throws IOException {
		return PieceStatus.NONE;
	}

	@Override
	public Module moduleOrigin() throws IOException {
		return new ModuleNone();
	}

	@Override
	public void count() throws IOException {
		
	}

	@Override
	public void update(LocalDate date, String reference, String origin, String notes, LocalDate dateEcheance, Tiers tiers) throws IOException {
		
	}

	@Override
	public Exercise exercise() throws IOException {
		return new ExerciseNone();
	}

	@Override
	public String origin() throws IOException {
		return null;
	}

	@Override
	public boolean isInvoiceOrPayment() throws IOException {
		return false;
	}

	@Override
	public PieceNature nature() throws IOException {
		return PieceNature.NONE;
	}

	@Override
	public void reconcile() throws IOException {
		
	}

	@Override
	public void validate() throws IOException {
		
	}

	@Override
	public PieceArticles articles() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : pièce inexistante !");
	}

	@Override
	public LocalDate dateEcheance() throws IOException {
		return null;
	}

	@Override
	public Tiers tiers() throws IOException {
		return new TiersNone();
	}
}
