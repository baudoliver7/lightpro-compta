package com.lightpro.compta.vm;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.compta.domains.api.Exercise;

public final class ExerciseVm {

	public final UUID id;
	public final String name;
	public final LocalDate start;
	public final LocalDate end;
	public final boolean isClosed;
	public final LocalDate openedDate;
	public final LocalDate closedDate;
	
	public ExerciseVm(){
		throw new UnsupportedOperationException("#ExerciseVm()");
	}
		
	public ExerciseVm(Exercise origin){
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.start = origin.period().start();
	        this.end = origin.period().end();
	        this.isClosed = origin.isClosed();
	        this.openedDate = origin.openedDate();
	        this.closedDate = origin.closedDate();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
