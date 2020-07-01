package com.compta.domains.api;

public enum JournalType {
	
	NONE       (0, "Aucun type"),
	VENTE      (1, "Vente"),
	ACHATS     (2, "Achat"),
	LIQUIDITES (3, "Caisse"),
	BANQUE     (4, "Banque"),
	DIVERS     (5, "Operations diverses");
	
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
