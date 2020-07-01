package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface Flux extends Nonable {
	UUID id();
	String object() throws IOException;
	JournalType journalType() throws IOException;
	Journal journal() throws IOException;
	PieceArticle article() throws IOException;
	int order() throws IOException;
	boolean isPrincipal() throws IOException;
	
	Lines lines() throws IOException;
	void validate() throws IOException;
	double debit() throws IOException;
	double credit() throws IOException;	
}
