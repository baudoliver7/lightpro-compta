package com.compta.interfacage.sales.api;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;

public interface InvoiceInterface {	
	UUID id();
	Journal journalVente() throws IOException;
	PieceType factureClient() throws IOException;
	Trame factureDoitTrame() throws IOException;
	ProductCategoryInterfaces productCategories() throws IOException;
	Compta module() throws IOException;
	
	void update(Journal journalVente, PieceType factureClient, Trame factureDoitTrame) throws IOException;
}
