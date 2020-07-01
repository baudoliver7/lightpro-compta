package com.compta.interfacage.sales.api;

import java.io.IOException;

import com.compta.domains.api.Account;

public interface ProductTypeInterface {
	int id();
	String name() throws IOException;
	Account account() throws IOException;
}
