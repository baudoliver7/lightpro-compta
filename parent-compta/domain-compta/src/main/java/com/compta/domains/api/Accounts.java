package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface Accounts extends AdvancedQueryable<Account, UUID>, Updatable<Account> {
	Account add(String code, String name, AccountType type, boolean refuseCreditBalance, boolean refuseDebitBalance) throws IOException;
	Account addAuxiliaryAccount(String code, String name, AccountType type, TiersType tiersType) throws IOException;
	Account get(String code) throws IOException;
	Account getAuxiliaryAccountOrDefault(TiersType type) throws IOException;
	
	Accounts of(TiersType type) throws IOException;
	Accounts of(AccountNature nature) throws IOException;
	Accounts of(Tiers tiers) throws IOException;
	Accounts of(AccountType type) throws IOException;
}
