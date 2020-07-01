package com.compta.domains.api;

import java.io.IOException;

public interface CompteResultatDetail {
	String code() throws IOException;
	String name() throws IOException;
	double amount() throws IOException;
	
	CompteResultat compteResultat() throws IOException;
}
