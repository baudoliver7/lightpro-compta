package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface Accounts extends AdvancedQueryable<Account, UUID>, Updatable<Account> {
	Account add(String code, String name, AccountType type) throws IOException;
}
