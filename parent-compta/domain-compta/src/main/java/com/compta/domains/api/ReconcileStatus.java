package com.compta.domains.api;

public enum ReconcileStatus {
	NONE       (0, "Aucun statut"),
	PARTIAL    (1, "Partielle"),
	COMPLETE   (2, "Complète"),
	LETTRATED  (3, "Lettrée");
	
	private final int id;
	private final String name;
	
	ReconcileStatus(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static ReconcileStatus get(int id){
		
		ReconcileStatus value = ReconcileStatus.NONE;
		for (ReconcileStatus item : ReconcileStatus.values()) {
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
