package com.compta.domains.api;

public enum PieceTypeUsageCode {
	
	USER_USAGE             (0, "Personnalisée"),
	CUSTOMER_INVOICE       (1, "Facture client"),
	CUSTOMER_PAYMENT       (2, "Règlement client"),
	SELLER_INVOICE         (3, "Facture fournisseur"),
	SELLER_PAYMENT         (4, "Règlement fournisseur");
	
	private final int id;
	private final String name;
	
	PieceTypeUsageCode(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static PieceTypeUsageCode get(int id){
		
		PieceTypeUsageCode value = PieceTypeUsageCode.USER_USAGE;
		for (PieceTypeUsageCode item : PieceTypeUsageCode.values()) {
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
