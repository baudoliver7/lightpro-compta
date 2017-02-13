package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Recordable;

public interface AccountChart extends Recordable<UUID, AccountChart> {
	String name() throws IOException;
	int codeDigits() throws IOException;
	Accounts accounts() throws IOException;
}
