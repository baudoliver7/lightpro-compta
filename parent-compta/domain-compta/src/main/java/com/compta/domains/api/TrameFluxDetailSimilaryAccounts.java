package com.compta.domains.api;

import java.io.IOException;

import com.infrastructure.core.GuidKeyQueryable;

public interface TrameFluxDetailSimilaryAccounts extends GuidKeyQueryable<Account> {
	void add(Account account) throws IOException;
	void deleteAll() throws IOException;	
}
