package com.lightpro.compta.cmd;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FacturationSaleInterfaceEdited {
	
	private final UUID journalVenteId;
	private final UUID factureClientId;
	private final UUID factureDoitTrameId;
	
	private final List<ProductCategorySaleInterfaceEdited> productCategories;
	
	public FacturationSaleInterfaceEdited(){
		throw new UnsupportedOperationException("#FacturationSaleInterfaceEdited()");
	}
	
	@JsonCreator
	public FacturationSaleInterfaceEdited( @JsonProperty("journalVenteId") final UUID journalVenteId, 
				    	  @JsonProperty("factureClientId") final UUID factureClientId,
				    	  @JsonProperty("factureDoitTrameId") final UUID factureDoitTrameId,				    	  
				    	  @JsonProperty("productCategories") final List<ProductCategorySaleInterfaceEdited> productCategories){
		
		this.productCategories = productCategories;
		this.journalVenteId = journalVenteId;
		this.factureClientId = factureClientId;
		this.factureDoitTrameId = factureDoitTrameId;
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
	
	public List<ProductCategorySaleInterfaceEdited> productCategories(){
		return productCategories;
	}
}
