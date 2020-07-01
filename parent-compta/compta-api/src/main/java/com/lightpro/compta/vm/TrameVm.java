package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.compta.domains.api.Trame;

public final class TrameVm {

	public final UUID id;
	public final List<TrameFluxVm> fluxes;
	public final String name;
	public final UUID pieceTypeId;
	public final String pieceType;
	
	public TrameVm(){
		throw new UnsupportedOperationException("#TrameVm()");
	}
		
	public TrameVm(Trame origin){
		try {
			this.id = origin.id();			
			this.fluxes = origin.fluxes().all().stream().map(m -> new TrameFluxVm(m)).collect(Collectors.toList());
			this.name = origin.name();
	        this.pieceTypeId = origin.pieceType().id();
	        this.pieceType = origin.pieceType().name();	        
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
}
