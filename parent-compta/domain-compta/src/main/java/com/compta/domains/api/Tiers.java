package com.compta.domains.api;

import java.io.IOException;

import com.securities.api.Contact;

public interface Tiers extends Contact {
	Accounts accounts() throws IOException;
}
