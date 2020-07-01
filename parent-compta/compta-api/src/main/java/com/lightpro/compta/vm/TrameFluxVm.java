package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.compta.domains.api.TrameFlux;

public final class TrameFluxVm {
	
	public final UUID id;
	public final boolean isPrincipal;
	public final String journalType;
	public final int journalTypeId;
	public final String trame;
	public final UUID trameId;
	public final String defaultJournal;
	public final UUID defaultJournalId;
	public final String description;
	public final List<TrameDetailVm> details;
	
	public TrameFluxVm(){
		throw new UnsupportedOperationException("#TrameFluxVm()");
	}
		
	public TrameFluxVm(TrameFlux origin){
		try {
			this.id = origin.id();
			this.isPrincipal = origin.isPrincipal();
			this.journalType = origin.journalType().toString();
	        this.journalTypeId = origin.journalType().id();
	        this.trame = origin.trame().name();
	        this.trameId = origin.trame().id();
	        this.defaultJournal = origin.defaultJournal().name();
	        this.defaultJournalId = origin.defaultJournal().id();
	        this.description = origin.description();
	        this.details = origin.details().all().stream().map(m -> new TrameDetailVm(m)).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
}
