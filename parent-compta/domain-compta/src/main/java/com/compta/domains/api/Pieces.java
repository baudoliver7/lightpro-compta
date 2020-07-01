package com.compta.domains.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Period;
import com.infrastructure.core.Updatable;
import com.securities.api.Module;

public interface Pieces extends AdvancedQueryable<Piece, UUID>, Updatable<Piece> {
	Piece add(UUID id, LocalDate date, String reference, String origin, String notes, Module moduleOrigin, LocalDate dateEcheance, Tiers tiers) throws IOException;
	Piece add(LocalDate date, String reference, String origin, String notes, Module moduleOrigin, LocalDate dateEcheance, Tiers tiers) throws IOException;
	Piece add(LocalDate date, String reference, String origin, String notes, LocalDate dateEcheance, Tiers tiers) throws IOException;
	
	Pieces of(PieceStatus status) throws IOException;
	Pieces of(PieceType pieceType) throws IOException;
	Pieces of(Period period) throws IOException;
	Pieces of(Module origin) throws IOException;
	Pieces withReference(String reference) throws IOException;
}
