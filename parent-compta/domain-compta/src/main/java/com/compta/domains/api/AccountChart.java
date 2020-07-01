package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface AccountChart extends Nonable {
	UUID id();
	String name() throws IOException;
	int codeDigits() throws IOException;
	Accounts accounts() throws IOException;
	Compta module() throws IOException;
	
	String accountCode(String codeBase) throws IOException;
}
