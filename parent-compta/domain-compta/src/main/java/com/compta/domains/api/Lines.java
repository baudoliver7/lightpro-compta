package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Period;
import com.infrastructure.core.Updatable;

public interface Lines extends AdvancedQueryable<Line, UUID>, Updatable<Line> {
	Line add(Tiers tiers, Account generalAccount, double debit, double credit) throws IOException;
	void deleteAll() throws IOException;
	
	void validate() throws IOException;
	
	Lines of(final Journal journal) throws IOException;
	Lines of(final Piece piece) throws IOException;
	Lines of(final PieceStatus pieceStatus) throws IOException;
	Lines of(final Period pieceDatePeriod) throws IOException;
	Lines of(final PieceNature pieceNature) throws IOException;
	Lines of(final LineStatus status) throws IOException;
	Lines of(final TiersType tiersType) throws IOException;
	Lines ofGeneralAccount(final Account account) throws IOException;
	Lines ofAuxiliaryAccount(final Account account) throws IOException;
	Lines of(final Flux flux) throws IOException;
	Lines withReference(final String reference) throws IOException;
	Lines withOrigin(final String origin) throws IOException;
	
	double debit() throws IOException;
	double credit() throws IOException;
	double balance() throws IOException;
}
