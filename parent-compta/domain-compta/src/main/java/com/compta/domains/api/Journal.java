package com.compta.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface Journal extends Nonable {
	UUID id();
	String code() throws IOException;
	String name() throws IOException;
	JournalType type() throws IOException;
	Account account() throws IOException;
	Compta module() throws IOException;
	List<PieceType> pieceTypes() throws IOException;
	boolean isViewedOnDashboard();
	boolean validateAccount();
	
	void viewOnDashboard(boolean active) throws IOException;
	void update(String code, String name, JournalType type, Account account, boolean validateAccount) throws IOException;
	void addPieceType(PieceType type) throws IOException;
	void removePieceType(PieceType type) throws IOException;
}
