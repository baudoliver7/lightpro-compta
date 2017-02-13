package com.compta.domains.api;

public enum AccountType {
	
	NONE                   (0, "Non défini"),
	RECEIVABLE             (1, "Recevable"),
	PAYABLE                (2, "Payable"),
	BANK_AND_CASH          (3, "Banque et liquidités"),
	CURRENT_ASSETS         (4, "Actifs actuels"),
	NON_CURRENT_ASSETS     (5, "Actifs non-courants"),
	PREPAYMENTS            (6, "Prépaiements"),
	FIXED_ASSETS           (7, "Immobilisations"),
	CURRENT_LIABILITIES    (8, "Passifs à court terme"),
	NON_CURRENT_LIABILITIES(9, "Passifs non-courants"),
	EQUITY                 (10, "Capitaux propres"),
	CURRENT_YEAR_EARNINGS  (11, "Bénéfices de l'année en cours"),
	OTHER_INCOME           (12, "Autre revenu"),
	INCOME                 (13, "Revenus"),
	DEPRECIATION           (14, "Dépréciation"),
	EXPENSES               (15, "Notes de frais"),
	COST_OF_REVENUE        (16, "Coûts directs"),
	COMPTES_SPECIAUX       (17, "Comptes spéciaux");
	
	private final int id;
	private final String name;
	
	AccountType(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static AccountType get(int id){
		
		AccountType value = AccountType.NONE;
		for (AccountType item : AccountType.values()) {
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
