package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class JournalPieceTypeMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public JournalPieceTypeMetadata() {
		this.domainName = "compta.journal_piecetypes";
		this.keyName = "id";
	}
	
	public JournalPieceTypeMetadata(final String domainName, final String keyName){
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
	
	public String journalIdKey(){
		return "journalid";
	}
	
	public String pieceTypeIdKey(){
		return "piecetypeid";
	}

	public static JournalPieceTypeMetadata create(){
		return new JournalPieceTypeMetadata();
	}	
}
