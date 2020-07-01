package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface Account extends Nonable {
	UUID id();
	String code() throws IOException;
	String name() throws IOException;
	String fullName() throws IOException;
	AccountType type() throws IOException;
	AccountChart chart() throws IOException;
	boolean isAuxiliary();
	boolean refuseCreditBalance() throws IOException;
	boolean refuseDebitBalance() throws IOException;
	TiersType tiersType() throws IOException;
	Lines lines() throws IOException;
	
	void update(String code, String name, AccountType type, boolean refuseCreditBalance, boolean refuseDebitBalance) throws IOException;
}
