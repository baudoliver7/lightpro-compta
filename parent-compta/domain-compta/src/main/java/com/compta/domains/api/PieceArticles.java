package com.compta.domains.api;

import java.io.IOException;

import com.infrastructure.core.GuidKeyQueryable;

public interface PieceArticles extends GuidKeyQueryable<PieceArticle> {
	void deleteAll() throws IOException;	
	PieceArticle add(PieceArticle article) throws IOException;
	PieceArticle add() throws IOException;
	void validate() throws IOException;
}
