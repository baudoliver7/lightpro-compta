package com.compta.domains.api;

public enum JournalType {
	
	NONE       (0, "Non défini"),
	VENTE      (1, "Vente"),
	ACHATS     (2, "Achats"),
	LIQUIDITES (3, "Liquidités"),
	BANQUE     (4, "Banque"),
	DIVERS     (5, "Divers");
	
	private final int id;
	private final String name;
	
	JournalType(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static JournalType get(int id){
		
		JournalType value = JournalType.NONE;
		for (JournalType item : JournalType.values()) {
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
