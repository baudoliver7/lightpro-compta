package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.PieceType;

public final class PieceTypeVm {

	public final UUID id;
	public final String name;
	public final boolean echeanceManaged;
	public final String tiersTypeManaged;
	public final UUID tiersTypeManagedId;
	public final String nature;
	public final int natureId;
	public final String sequence;
	public final UUID sequenceId;
	public final String object;
	public final String preferredTrame;
	public final UUID preferredTrameId;
	
	public PieceTypeVm(){
		throw new UnsupportedOperationException("#PieceTypeVm()");
	}
		
	public PieceTypeVm(PieceType origin){
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.echeanceManaged = origin.echeanceManaged();
	        this.tiersTypeManaged = origin.tiersTypeManaged().name();
	        this.tiersTypeManagedId = origin.tiersTypeManaged().id();
	        this.nature = origin.nature().toString();
	        this.natureId = origin.nature().id();
	        this.sequence = origin.sequence().name();
	        this.sequenceId = origin.sequence().id();
	        this.object = origin.object();
	        this.preferredTrame = origin.preferredTrame().name();
	        this.preferredTrameId = origin.preferredTrame().id();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
}
