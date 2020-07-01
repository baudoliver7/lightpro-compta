package com.lightpro.compta.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PdvPaymentModeSaleInterfaceEdited {
	
	private final UUID id;
	private final UUID journalEncaissementId;
	private final UUID reglementId;
	private final UUID acompteOrAvanceId;
	private final UUID reglementDefinitifId;
	private final UUID provisionId;
	
	public PdvPaymentModeSaleInterfaceEdited(){
		throw new UnsupportedOperationException("#PdvPaymentModeSaleInterfaceEdited()");
	}
	
	@JsonCreator
	public PdvPaymentModeSaleInterfaceEdited( @JsonProperty("id") final UUID id,
						  @JsonProperty("journalEncaissementId") final UUID journalEncaissementId,
						  @JsonProperty("reglementId") final UUID reglementId,
				    	  @JsonProperty("acompteOrAvanceId") final UUID acompteOrAvanceId,
				    	  @JsonProperty("reglementDefinitifId") final UUID reglementDefinitifId,
				    	  @JsonProperty("provisionId") final UUID provisionId){
		
		this.id = id;
		this.journalEncaissementId = journalEncaissementId;		
		this.reglementId = reglementId;
		this.acompteOrAvanceId = acompteOrAvanceId;
		this.reglementDefinitifId = reglementDefinitifId;
		this.provisionId = provisionId;
	}
	
	public UUID id(){
		return id;
	}
	
	public UUID journalEncaissementId(){
		return journalEncaissementId;
	}	
	
	public UUID reglementId(){
		return reglementId;
	}
	
	public UUID acompteOrAvanceId(){
		return acompteOrAvanceId;
	}
	
	public UUID reglementDefinitifId(){
		return reglementDefinitifId;
	}
	
	public UUID provisionId(){
		return provisionId;
	}
}
