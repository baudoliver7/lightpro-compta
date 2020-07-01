package com.compta.domains.api;

public enum PieceNature {
	
	NONE      (0, "Inconnue"),	
	INVOICE   (2, "Facture"),
	PAYMENT   (4, "Règlement"),
	RELEVE   (7, "Relevé"),
	INTERNAL_TRANSFER   (6, "Transfert interne"),
	OTHER     (1, "Divers");
	
	private final int id;
	private final String name;
	
	PieceNature(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static PieceNature get(int id){
		
		PieceNature value = PieceNature.NONE;
		for (PieceNature item : PieceNature.values()) {
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
