package com.compta.domains.api;

public enum PieceStatus {
	NONE        (0, "Aucun statut"),
	UNACCOUNTED (1, "Décomptabilisée"),
	ACCOUNTED   (2, "Comptabilisée");
	
	private final int id;
	private final String name;
	
	PieceStatus(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static PieceStatus get(int id){
		
		PieceStatus value = PieceStatus.NONE;
		for (PieceStatus item : PieceStatus.values()) {
			if(item.id() == id)
				value = item;
		}
		
		return value;
	}

	public int id(){
		return id;
	}
	
	public String toString(){
		return name;
	}
}
