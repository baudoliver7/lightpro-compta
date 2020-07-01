package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.compta.domains.api.Account;
import com.compta.domains.api.AccountNature;
import com.compta.domains.api.AccountType;
import com.compta.domains.api.CompteResultat;
import com.compta.domains.api.CompteResultatDetail;
import com.compta.domains.api.Exercise;

public final class CompteResultatImpl implements CompteResultat {

	private transient final Exercise exercise;
	
	public CompteResultatImpl(Exercise exercise){
		this.exercise = exercise;
	}
	
	@Override
	public List<CompteResultatDetail> produitsExploitation() throws IOException {
		List<CompteResultatDetail> details = new ArrayList<CompteResultatDetail>();
		
		for (Account account : exercise.module().accounts().of(AccountNature.GENERAL).of(AccountType.OPERATING_INCOME).all()) {
			CompteResultatDetail detail = new CompteResultatDetailImpl(account, exercise);
			if(detail.amount() != 0)
				details.add(detail);
		}
		
		return details;
	}

	@Override
	public List<CompteResultatDetail> chargesExploitation() throws IOException {

		List<CompteResultatDetail> details = new ArrayList<CompteResultatDetail>();
		
		for (Account account : exercise.module().accounts().of(AccountNature.GENERAL).of(AccountType.OPERATING_EXPENSES).all()) {
			CompteResultatDetail detail = new CompteResultatDetailImpl(account, exercise);
			if(detail.amount() != 0)
				details.add(detail);
		}
		
		return details;
	}

	@Override
	public double montantProduitsExploitation() throws IOException {
		double sum = 0;
		
		for (CompteResultatDetail detail : produitsExploitation()) {
			sum += detail.amount();
		}
		
		return sum;
	}

	@Override
	public double montantChargesExploitation() throws IOException {
		double sum = 0;
		
		for (CompteResultatDetail detail : chargesExploitation()) {
			sum += detail.amount();
		}
		
		return sum;
	}

	@Override
	public double resultatExploitation() throws IOException {
		return montantProduitsExploitation() - montantChargesExploitation();
	}

	@Override
	public List<CompteResultatDetail> produitsFinanciers() throws IOException {

		List<CompteResultatDetail> details = new ArrayList<CompteResultatDetail>();
		
		for (Account account : exercise.module().accounts().of(AccountNature.GENERAL).of(AccountType.FINANCIAL_INCOME).all()) {
			CompteResultatDetail detail = new CompteResultatDetailImpl(account, exercise);
			if(detail.amount() != 0)
				details.add(detail);
		}
		
		return details;
	}

	@Override
	public List<CompteResultatDetail> chargesFinancieres() throws IOException {
		List<CompteResultatDetail> details = new ArrayList<CompteResultatDetail>();
		
		for (Account account : exercise.module().accounts().of(AccountNature.GENERAL).of(AccountType.FINANCIAL_EXPENSES).all()) {
			CompteResultatDetail detail = new CompteResultatDetailImpl(account, exercise);
			if(detail.amount() != 0)
				details.add(detail);
		}
		
		return details;
	}

	@Override
	public double montantProduitsFinanciers() throws IOException {
		double sum = 0;
		
		for (CompteResultatDetail detail : produitsFinanciers()) {
			sum += detail.amount();
		}
		
		return sum; 
	}

	@Override
	public double montantChargesFinancieres() throws IOException {
		double sum = 0;
		
		for (CompteResultatDetail detail : chargesFinancieres()) {
			sum += detail.amount();
		}
		
		return sum;
	}

	@Override
	public double resultatFinancier() throws IOException {
		return montantProduitsFinanciers() - montantChargesFinancieres();
	}

	@Override
	public List<CompteResultatDetail> produitsExceptionnels() throws IOException {
		List<CompteResultatDetail> details = new ArrayList<CompteResultatDetail>();
		
		for (Account account : exercise.module().accounts().of(AccountNature.GENERAL).of(AccountType.EXTRA_INCOME).all()) {
			CompteResultatDetail detail = new CompteResultatDetailImpl(account, exercise);
			if(detail.amount() != 0)
				details.add(detail);
		}
		
		return details;
	}

	@Override
	public List<CompteResultatDetail> chargesExceptionnelles() throws IOException {
		List<CompteResultatDetail> details = new ArrayList<CompteResultatDetail>();
		
		for (Account account : exercise.module().accounts().of(AccountNature.GENERAL).of(AccountType.EXTRA_EXPENSES).all()) {
			CompteResultatDetail detail = new CompteResultatDetailImpl(account, exercise);
			if(detail.amount() != 0)
				details.add(detail);
		}
		
		return details;
	}

	@Override
	public double montantProduitsExceptionnels() throws IOException {
		double sum = 0;
		
		for (CompteResultatDetail detail : produitsExceptionnels()) {
			sum += detail.amount();
		}
		
		return sum;
	}

	@Override
	public double montantChargesExceptionnelles() throws IOException {
		double sum = 0;
		
		for (CompteResultatDetail detail : chargesExceptionnelles()) {
			sum += detail.amount();
		}
		
		return sum;
	}

	@Override
	public double resultatExceptionnel() throws IOException {
		return montantProduitsExceptionnels() - montantChargesExceptionnelles();
	}

	@Override
	public double participationDesSalaries() throws IOException {
		double sum = 0;
		
		for (Account account : exercise.module().accounts().of(AccountNature.GENERAL).of(AccountType.PARTICIPATION_SALARIE).all()) {
			sum += account.lines().of(exercise.period()).balance();
		}
		
		return sum;
	}

	@Override
	public double impotsSurLesBenefices() throws IOException {
		double sum = 0;
		
		for (Account account : exercise.module().accounts().of(AccountNature.GENERAL).of(AccountType.IMPOTS_SUR_BENEFICES).all()) {
			sum += account.lines().of(exercise.period()).balance();
		}
		
		return sum;
	}

	@Override
	public double montantProduits() throws IOException {
		return montantProduitsExploitation() + montantProduitsFinanciers() + montantProduitsExceptionnels();
	}

	@Override
	public double montantCharges() throws IOException {
		return montantChargesExploitation() + montantChargesFinancieres() + montantChargesExceptionnelles() + participationDesSalaries() + impotsSurLesBenefices();
	}

	@Override
	public double resultatNet() throws IOException {
		return montantProduits() - montantCharges();
	}

	@Override
	public Exercise exercise() throws IOException {
		return exercise;
	}
}
