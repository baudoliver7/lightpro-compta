package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.PieceTypeUsageCode;
import com.compta.domains.api.Pieces;
import com.compta.domains.api.TiersType;
import com.compta.domains.api.Trame;
import com.compta.domains.api.Trames;
import com.infrastructure.core.GuidKeyEntityNone;
import com.securities.api.Sequence;
import com.securities.impl.SequenceNone;

public final class PieceTypeNone extends GuidKeyEntityNone<PieceType> implements PieceType {

	@Override
	public String name() throws IOException {
		return "Aucun type";
	}

	@Override
	public PieceTypeUsageCode usageCode() throws IOException {
		return PieceTypeUsageCode.USER_USAGE;
	}

	@Override
	public  Trame preferredTrame() throws IOException {
		return new TrameNone();
	}

	@Override
	public boolean echeanceManaged() throws IOException {
		return false;
	}

	@Override
	public TiersType tiersTypeManaged() throws IOException {
		return new TiersTypeNone();
	}

	@Override
	public Pieces pieces() throws IOException {
		return null;
	}

	@Override
	public Compta module() throws IOException {
		return new ComptaNone();
	}

	@Override
	public void update(String name, TiersType tiersTypeManaged, PieceNature nature, Sequence sequence, String object, Trame preferredTrame) throws IOException {
		 
	}

	@Override
	public PieceNature nature() throws IOException {
		return PieceNature.NONE;
	}

	@Override
	public String object() throws IOException {
		return null;
	}

	@Override
	public Sequence sequence() throws IOException {
		return new SequenceNone();
	}

	@Override
	public Trames trames() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : le type de pièce inexistant !");
	}

	@Override
	public List<Journal> journals() throws IOException {
		return new ArrayList<Journal>();
	}

	@Override
	public void changePreferredTrame(Trame trame) throws IOException {
		
	}
}
