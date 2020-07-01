package com.compta.domains.api;

public enum OperationSens {
	NONE       (0, "Non défini"),
	DEBIT      (1, "Débit"),
	CREDIT     (2, "Crédit");
	
	private final int id;
	private final String name;
	
	OperationSens(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static OperationSens get(int id){
		
		OperationSens value = OperationSens.NONE;
		for (OperationSens item : OperationSens.values()) {
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
