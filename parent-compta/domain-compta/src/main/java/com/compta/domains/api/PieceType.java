package com.compta.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.Nonable;
import com.securities.api.Sequence;

public interface PieceType extends Nonable {
	UUID id();
	String name() throws IOException;
	PieceTypeUsageCode usageCode() throws IOException;
	Trames trames() throws IOException;
	Trame preferredTrame() throws IOException;
	TiersType tiersTypeManaged() throws IOException;
	boolean echeanceManaged() throws IOException;
	Pieces pieces() throws IOException;	
	PieceNature nature() throws IOException;
	String object() throws IOException;
	Sequence sequence() throws IOException;
	Compta module() throws IOException;
	List<Journal> journals() throws IOException;
	
	void update(String name, TiersType tiersTypeManaged, PieceNature nature, Sequence sequence, String object, Trame preferredTrame) throws IOException;
	void changePreferredTrame(Trame trame) throws IOException;
}
