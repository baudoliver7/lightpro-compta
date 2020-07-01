package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;
import com.securities.api.Sequence;

public interface PieceTypes extends AdvancedQueryable<PieceType, UUID>, Updatable<PieceType> {
	PieceType add(String name, PieceTypeUsageCode usageCode, TiersType tiersTypeManaged, PieceNature nature, Sequence sequence, String object) throws IOException;
	PieceType add(String name, TiersType tiersTypeManaged, PieceNature nature, Sequence sequence, String object) throws IOException;
	PieceType none();
}
