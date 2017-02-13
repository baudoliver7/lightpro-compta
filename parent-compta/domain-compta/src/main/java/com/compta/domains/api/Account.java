package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Recordable;

public interface Account extends Recordable<UUID, Account> {
	String code() throws IOException;
	String name() throws IOException;
	AccountType type() throws IOException;
	AccountChart chart() throws IOException;
	
	void update(String code, String name, AccountType type) throws IOException;
}
