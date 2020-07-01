package com.compta.domains.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.infrastructure.core.Nonable;
import com.infrastructure.core.Period;

public interface Exercise extends Nonable {	
	UUID id();
	String name() throws IOException;
	Period period() throws IOException;
	boolean isClosed() throws IOException;
	LocalDate openedDate() throws IOException;
	LocalDate closedDate() throws IOException;
	Compta module() throws IOException;
	Pieces pieces() throws IOException;
	CompteResultat compteResultat() throws IOException;
	
	void update(Period period) throws IOException;
	void close() throws IOException;
	
	public enum ExerciseStatus {
		NONE,
		UNCLOSED,
		CLOSED
	}
}
