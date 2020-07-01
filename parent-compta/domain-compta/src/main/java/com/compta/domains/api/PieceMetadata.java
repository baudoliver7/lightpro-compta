package com.compta.domains.api;


import com.infrastructure.core.DomainMetadata;

public class PieceMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public PieceMetadata() {
		this.domainName = "compta.pieces";
		this.keyName = "id";
	}
	
	public PieceMetadata(final String domainName, final String keyName){
		this.domainName = domainName;
		this.keyName = keyName;
	}
	
	@Override
	public String domainName() {
		return this.domainName;
	}

	@Override
	public String keyName() {
		return this.keyName;
	}
	
	public String pieceDateKey(){
		return "piecedate";
	}
	
	public String typeIdKey(){
		return "typeid";
	}
	
	public String referenceKey(){
		return "reference";
	}
	
	public String notesKey(){
		return "notes";
	}
	
	public String statusKey(){
		return "status";
	}
	
	public String originModuleIdKey(){
		return "origin_moduleid";
	}
	
	public String originKey(){
		return "origin";
	}
	
	public String dateEcheanceKey(){
		return "date_echeance";
	}
	
	public String tiersIdKey(){
		return "tiersid";
	}

	public static PieceMetadata create(){
		return new PieceMetadata();
	}	
}
