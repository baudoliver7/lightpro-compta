package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.compta.domains.api.Flux;

public final class FluxVm {
	
	public final UUID id;
	public final String journalType;
	public final int journalTypeId;
	public final String journal;
	public final UUID journalId;
	public final List<LineVm> lines;
	public final String object;
	public final int order;
	
	public FluxVm() {
		throw new UnsupportedOperationException("#FluxVm()");
	}
		
	public FluxVm(Flux origin){
		try {
			this.id = origin.id();
			this.journalType = origin.journalType().toString();
			this.journalTypeId = origin.journalType().id();
	        this.journal = origin.journal().name();
	        this.journalId = origin.journal().id();
	        this.lines = origin.lines().all().stream().map(m -> new LineVm(m)).collect(Collectors.toList());
	        this.object = origin.object();
	        this.order = origin.order();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
