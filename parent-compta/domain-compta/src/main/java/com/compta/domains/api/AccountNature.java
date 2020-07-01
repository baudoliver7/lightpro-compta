package com.compta.domains.api;

public enum AccountNature {
	NONE       (0, "Non défini"),
	GENERAL    (1, "Général"),
	AUXILIARY  (2, "Auxiliaire"),;
	
	private final int id;
	private final String name;
	
	AccountNature(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static AccountNature get(int id){
		
		AccountNature value = AccountNature.NONE;
		for (AccountNature item : AccountNature.values()) {
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
