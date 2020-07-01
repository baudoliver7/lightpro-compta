package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;

import com.compta.domains.api.Compta;
import com.compta.domains.api.CompteResultat;
import com.compta.domains.api.Exercise;
import com.compta.domains.api.Pieces;
import com.infrastructure.core.GuidKeyEntityNone;
import com.infrastructure.core.Period;
import com.infrastructure.core.impl.PeriodNone;

public final class ExerciseNone extends GuidKeyEntityNone<Exercise> implements Exercise {

	@Override
	public Period period() throws IOException {
		return new PeriodNone();
	}

	@Override
	public boolean isClosed() throws IOException {		
		return false;
	}

	@Override
	public LocalDate openedDate() throws IOException {
		return null;
	}

	@Override
	public LocalDate closedDate() throws IOException {
		return null;
	}

	@Override
	public Compta module() throws IOException {
		return new ComptaNone();
	}

	@Override
	public void update(Period period) throws IOException {
		 
	}

	@Override
	public void close() throws IOException {
		
	}

	@Override
	public Pieces pieces() throws IOException {
		throw new IllegalArgumentException("Aucune pièce disponible : l'exercice comptable n'est pas défini !"); 
	}

	@Override
	public CompteResultat compteResultat() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée : l'exercice comptable n'est pas défini !");
	}

	@Override
	public String name() throws IOException {
		return "Aucun exercise";
	}
}
