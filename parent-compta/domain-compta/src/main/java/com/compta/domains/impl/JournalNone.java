package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.PieceType;
import com.infrastructure.core.GuidKeyEntityNone;

public final class JournalNone extends GuidKeyEntityNone<Journal> implements Journal {

	@Override
	public String code() throws IOException {
		return "AUJN";
	}

	@Override
	public String name() throws IOException {
		return "Aucun journal";
	}

	@Override
	public JournalType type() throws IOException {
		return JournalType.DIVERS;
	}

	@Override
	public Account account() throws IOException {
		return new AccountNone();
	}

	@Override
	public Compta module() throws IOException {
		return new ComptaNone();
	}

	@Override
	public void update(String code, String name, JournalType type, Account account, boolean validateAccount) throws IOException {
		
	}

	@Override
	public boolean isViewedOnDashboard() {
		return false;
	}

	@Override
	public void viewOnDashboard(boolean active) throws IOException {
		 
	}

	@Override
	public List<PieceType> pieceTypes() throws IOException {
		return new ArrayList<PieceType>();
	}

	@Override
	public void addPieceType(PieceType type) throws IOException {
		
	}

	@Override
	public void removePieceType(PieceType type) throws IOException {
		
	}

	@Override
	public boolean validateAccount() {
		return false;
	}
}
