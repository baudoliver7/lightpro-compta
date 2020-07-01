package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.compta.domains.api.PieceArticle;

public final class PieceArticleVm {
	
	public final UUID id;
	public final List<FluxVm> fluxes;
	public final int order;
	
	public PieceArticleVm() {
		throw new UnsupportedOperationException("#PieceArticleVm()");
	}
		
	public PieceArticleVm(PieceArticle origin){
		try {
			this.id = origin.id();
			this.fluxes = origin.fluxes().all().stream().map(m -> new FluxVm(m)).collect(Collectors.toList());
			this.order = origin.order();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
}
