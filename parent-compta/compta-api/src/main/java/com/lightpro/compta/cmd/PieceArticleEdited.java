package com.lightpro.compta.cmd;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PieceArticleEdited {
	
	private final List<FluxEdited> fluxes;
	
	public PieceArticleEdited(){
		throw new UnsupportedOperationException("#PieceArticleEdited()");
	}
	
	@JsonCreator
	public PieceArticleEdited(@JsonProperty("fluxes") final List<FluxEdited> fluxes){			
		this.fluxes = fluxes;
	}
	
	public List<FluxEdited> fluxes(){
		return fluxes;
	}
}
