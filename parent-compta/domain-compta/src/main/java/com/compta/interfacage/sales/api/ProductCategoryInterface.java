package com.compta.interfacage.sales.api;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;

public interface ProductCategoryInterface {	
	UUID id();
	public abstract String name() throws IOException;
	public abstract Journal journalVente() throws IOException;
	public abstract PieceType factureClient() throws IOException;
	public abstract Trame factureDoitTrame() throws IOException;
	
	public abstract void update(Journal journalVente, PieceType factureClient, Trame factureDoitTrame) throws IOException;
}
