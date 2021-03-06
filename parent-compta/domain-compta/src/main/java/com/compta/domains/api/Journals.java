package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface Journals extends AdvancedQueryable<Journal, UUID>, Updatable<Journal> {	
	Journal add(String code, String name, JournalType type, Account account, boolean validateAccount) throws IOException;
	Journal none();
	
	Journals of(JournalType journalType) throws IOException;
}
