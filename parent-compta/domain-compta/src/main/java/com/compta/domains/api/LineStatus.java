package com.compta.domains.api;

public enum LineStatus {
	NONE         (0, "Non défini"),
	UNRECONCILED (1, "Non reconciliée"),
	RECONCILED   (2, "Reconciliée"),
	UNLETTRATED  (3, "Non lettrée"),
	LETTRATED    (4, "Lettrée");
	
	private final int id;
	private final String name;
	
	LineStatus(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static LineStatus get(int id){
		
		LineStatus value = LineStatus.NONE;
		for (LineStatus item : LineStatus.values()) {
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
