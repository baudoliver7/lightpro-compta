package com.compta.domains.api;

import java.io.IOException;
import java.util.List;

public interface CompteResultat {
	
	Exercise exercise() throws IOException;
	List<CompteResultatDetail> produitsExploitation() throws IOException;
	List<CompteResultatDetail> chargesExploitation() throws IOException;
	
	double montantProduitsExploitation() throws IOException;
	double montantChargesExploitation() throws IOException;	
	double resultatExploitation() throws IOException;
	
	List<CompteResultatDetail> produitsFinanciers() throws IOException;
	List<CompteResultatDetail> chargesFinancieres() throws IOException;
	
	double montantProduitsFinanciers() throws IOException;
	double montantChargesFinancieres() throws IOException;	
	double resultatFinancier() throws IOException;
	
	List<CompteResultatDetail> produitsExceptionnels() throws IOException;
	List<CompteResultatDetail> chargesExceptionnelles() throws IOException;
	
	double montantProduitsExceptionnels() throws IOException;
	double montantChargesExceptionnelles() throws IOException;	
	double resultatExceptionnel() throws IOException;
	
	double participationDesSalaries() throws IOException;
	double impotsSurLesBenefices() throws IOException;
	
	double montantProduits() throws IOException;
	double montantCharges() throws IOException;	
	double resultatNet() throws IOException;
}
