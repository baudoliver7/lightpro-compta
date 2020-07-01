package com.lightpro.compta.cmd;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import com.common.utilities.convert.TimeConvert;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PieceEdited {
	
	private final LocalDate date;
	private final String name;
	private final UUID typeId;
	private final String reference;
	private final String origin;
	private final String notes;
	private final LocalDate dateEcheance;
	private final UUID tiersId;
	
	private final List<PieceArticleEdited> articles;
	
	public PieceEdited(){
		throw new UnsupportedOperationException("#PieceEdited()");
	}
	
	@JsonCreator
	public PieceEdited( @JsonProperty("date") final Date date,
						@JsonProperty("name") final String name, 
				    	@JsonProperty("typeId") final UUID typeId,
				    	@JsonProperty("reference") final String reference,
				    	@JsonProperty("origin") final String origin,
				    	@JsonProperty("notes") final String notes,
				    	@JsonProperty("dateEcheance") final Date dateEcheance,
				    	@JsonProperty("tiersId") final UUID tiersId,				    	
				    	@JsonProperty("articles") final List<PieceArticleEdited> articles){
		
		this.name = name;
		this.date = TimeConvert.toLocalDate(date, ZoneId.systemDefault());
		this.typeId = typeId;
		this.reference = reference;
		this.origin = origin;
		this.notes = notes;
		this.dateEcheance = TimeConvert.toLocalDate(dateEcheance, ZoneId.systemDefault());
		this.tiersId = tiersId;
		this.articles = articles;
	}
	
	public LocalDate date(){
		return date;
	}
	
	public String name(){
		return name;
	}
	
	public String reference(){
		return reference;
	}
	
	public String origin(){
		return origin;
	}
	
	public String notes(){
		return notes;
	}
	
	public UUID typeId(){
		return typeId;
	}
	
	public LocalDate dateEcheance(){
		return dateEcheance;
	}
	
	public UUID tiersId(){
		return tiersId;
	}
	
	public List<PieceArticleEdited> articles(){
		return articles;
	}
}
