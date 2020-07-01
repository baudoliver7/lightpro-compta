package com.compta.domains.api;

public enum TiersCode {
	
	CUSTOMER    (1, "Client"),
	FOURNISSEUR (2, "Fournisseur"),
	OTHER       (3, "Divers");
	
	private final int id;
	private final String name;
	
	TiersCode(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static TiersCode get(int id){
		
		TiersCode value = TiersCode.OTHER;
		for (TiersCode item : TiersCode.values()) {
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
