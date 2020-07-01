package com.lightpro.compta.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductCategorySaleInterfaceEdited {
	
	private final UUID id;
	private final UUID journalVenteId;
	private final UUID factureClientId;
	private final UUID factureDoitTrameId;
	
	public ProductCategorySaleInterfaceEdited(){
		throw new UnsupportedOperationException("#ProductCategorySaleInterfaceEdited()");
	}
	
	@JsonCreator
	public ProductCategorySaleInterfaceEdited( @JsonProperty("id") final UUID id,
						  @JsonProperty("journalVenteId") final UUID journalVenteId,
						  @JsonProperty("factureClientId") final UUID factureClientId,
				    	  @JsonProperty("factureDoitTrameId") final UUID factureDoitTrameId){
		
		this.id = id;
		this.journalVenteId = journalVenteId;		
		this.factureClientId = factureClientId;
		this.factureDoitTrameId = factureDoitTrameId;
	}
	
	public UUID id(){
		return id;
	}
	
	public UUID journalVenteId(){
		return journalVenteId;
	}	
	
	public UUID factureClientId(){
		return factureClientId;
	}
	
	public UUID factureDoitTrameId(){
		return factureDoitTrameId;
	}
}
