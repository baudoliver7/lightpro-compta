package com.lightpro.compta.vm;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.compta.domains.api.Piece;

public final class PieceVm {
	
	public final UUID id;
	public final String type;
	public final UUID typeId;
	public final String tiersTypeManaged;
	public final UUID tiersTypeManagedId;
	public final boolean echeanceManaged;
	public final LocalDate date;
	public final String reference;
	public final String origin;
	public final List<PieceArticleVm> articles;
	public final String status;
	public final int statusId;
	public final String nature;
	public final int natureId;
	public final String notes;
	public final LocalDate dateEcheance;
	public final String tiers;
	public final UUID tiersId;
	
	public PieceVm(){
		throw new UnsupportedOperationException("#PieceVm()");
	}
		
	public PieceVm(Piece origin){
		try {
			this.id = origin.id();
			this.typeId = origin.type().id();
	        this.type = origin.type().name();
			this.tiersTypeManaged = origin.type().tiersTypeManaged().name();
			this.tiersTypeManagedId = origin.type().tiersTypeManaged().id();
	        this.echeanceManaged = origin.type().echeanceManaged();
	        this.date = origin.date();
	        this.reference = origin.reference();
	        this.origin = origin.origin();
	        this.articles = origin.articles().all().stream().map(m -> new PieceArticleVm(m)).collect(Collectors.toList());
	        this.status = origin.status().toString();
	        this.statusId = origin.status().id();
	        this.nature = origin.nature().toString();
	        this.natureId = origin.nature().id();
	        this.notes = origin.notes();
	        this.dateEcheance = origin.dateEcheance();
	        this.tiers = origin.tiers().name();
	        this.tiersId = origin.tiers().id();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
}
