package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Recordable;

public interface Journal extends Recordable<UUID, Journal> {
	String code() throws IOException;
	String name() throws IOException;
	JournalType type() throws IOException;
	Compta module() throws IOException;
	
	void update(String code, String name, JournalType type) throws IOException;
}
