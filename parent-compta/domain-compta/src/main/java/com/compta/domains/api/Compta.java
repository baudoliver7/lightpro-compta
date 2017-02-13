package com.compta.domains.api;

import java.io.IOException;

import com.securities.api.Module;

public interface Compta extends Module {
	AccountChart chart() throws IOException;
	Journals journals() throws IOException;
}
