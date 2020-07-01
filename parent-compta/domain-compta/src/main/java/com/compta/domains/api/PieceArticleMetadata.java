package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public final class PieceArticleMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public PieceArticleMetadata() {
		this.domainName = "compta.piece_articles";
		this.keyName = "id";
	}
	
	public PieceArticleMetadata(final String domainName, final String keyName){
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
	
	public String pieceIdKey(){
		return "pieceid";
	}
	
	public String orderKey(){
		return "order_prop";
	}
	
	public static PieceArticleMetadata create(){
		return new PieceArticleMetadata();
	}	
}
