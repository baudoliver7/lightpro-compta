package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;
import com.securities.api.Sequence;

public interface TiersType extends Nonable {
	UUID id();
	TiersCode code() throws IOException;
	String name() throws IOException;
	Account generalAccount() throws IOException;
	Compta module() throws IOException;
	Sequence sequence() throws IOException;
	Sequence sequenceLettrage() throws IOException;
	Reconciles reconciles() throws IOException;
	Lines lines() throws IOException;
	
	void update(String name, Account generalAccount, Sequence sequence, Sequence sequenceLettrage) throws IOException;
}
