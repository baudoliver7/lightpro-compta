package com.compta.domains.api;

public enum AccountType {
	
	NONE                   (0,  "Non d�fini"),
	ANONYMOUS              (26,  "Anonyme"),
	RECEIVABLE             (1,  "Recevable"),
	PAYABLE                (2,  "Payable"),
	BANK_AND_CASH          (3,  "Banque et liquidit�s"),
	CURRENT_ASSETS         (4,  "Actifs actuels"),
	NON_CURRENT_ASSETS     (5,  "Actifs non-courants"),
	PREPAYMENTS            (6,  "Pr�paiements"),
	INCORPOREL_FIXED_ASSETS(7,  "Immobilisations incorporelles"),
	CORPOREL_FIXED_ASSETS  (22, "Immobilisations corporelles"),
	FINANCIAL_FIXED_ASSETS (23, "Immobilisations financi�res"),
	CURRENT_LIABILITIES    (8,  "Passifs � court terme"),
	NON_CURRENT_LIABILITIES(9,  "Passifs non-courants"),
	EQUITY                 (10, "Capitaux propres"),
	CURRENT_YEAR_EARNINGS  (11, "B�n�fices de l'ann�e en cours"),
	//OTHER_INCOME           (12, "Autre revenu"),
	OPERATING_INCOME       (13, "Revenus d'exploitation"),
	FINANCIAL_INCOME       (18, "Revenus financiers"),
	EXTRA_INCOME           (19, "Revenus exceptionnels"),
	DEPRECIATION           (14, "D�pr�ciation"),
	OPERATING_EXPENSES     (15, "Charges d'exploitation"),
	FINANCIAL_EXPENSES     (20, "Charges financi�res"),
	EXTRA_EXPENSES         (21, "Charges exceptionnelles"),
	PARTICIPATION_SALARIE  (24, "Participation des salari�s"),
	IMPOTS_SUR_BENEFICES   (25, "Imp�ts sur les b�n�fices"),
	//COST_OF_REVENUE        (16, "Co�ts directs"),
	COMPTES_SPECIAUX       (17, "Comptes sp�ciaux");
	
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
