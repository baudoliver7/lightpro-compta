package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Exercise;
import com.compta.domains.api.Flux;
import com.compta.domains.api.LineStatus;
import com.compta.domains.api.Line;
import com.compta.domains.api.Piece;
import com.compta.domains.api.PieceArticle;
import com.compta.domains.api.PieceArticles;
import com.compta.domains.api.PieceMetadata;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.PieceStatus;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Reconcile;
import com.compta.domains.api.Tiers;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.securities.api.Module;

public final class PieceDb extends GuidKeyEntityDb<Piece, PieceMetadata> implements Piece {
	
	private final Compta module;
	
	public PieceDb(final Base base, final UUID id, final Compta module){
		super(base, id, "Pièce introuvable !");
		this.module = module;
	}

	@Override
	public LocalDate date() throws IOException {
		java.sql.Date date = ds.get(dm.pieceDateKey());
		return date.toLocalDate();
	}

	@Override
	public PieceType type() throws IOException {
		UUID typeId = ds.get(dm.typeIdKey());
		return new PieceTypeDb(base, typeId, module);
	}

	@Override
	public String reference() throws IOException {
		return ds.get(dm.referenceKey());
	}

	@Override
	public String notes() throws IOException {
		return ds.get(dm.notesKey());
	}

	@Override
	public PieceStatus status() throws IOException {
		int statusId = ds.get(dm.statusKey());
		return PieceStatus.get(statusId);
	}

	@Override
	public Module moduleOrigin() throws IOException {
		UUID moduleId = ds.get(dm.originModuleIdKey());
		return module.company().modulesInstalled().get(moduleId);
	}

	public static void validate(LocalDate date, String reference, PieceType pieceType, LocalDate dateEcheance) throws IOException {
		
		if(date == null)
			throw new IllegalArgumentException("Vous devez renseigner la date de la pièce !");
		
		if(StringUtils.isBlank(reference))
			throw new IllegalArgumentException("Vous devez renseigner la référence de la pièce !");
		
		if(pieceType.isNone())
			throw new IllegalArgumentException("Vous devez spécifier un type de pièce !");
		
		if(dateEcheance == null && pieceType.nature() == PieceNature.INVOICE)
			throw new IllegalArgumentException("Vous devez renseigner la date d'échéance de la facture !");
	}
	
	@Override
	public void update(LocalDate date, String reference, String origin, String notes, LocalDate dateEcheance, Tiers tiers) throws IOException {
		
		validate(date, reference, type(), dateEcheance);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.referenceKey(), reference);
		params.put(dm.pieceDateKey(), java.sql.Date.valueOf(date));
		params.put(dm.originKey(), origin);
		params.put(dm.notesKey(), notes);
		
		if(status() == PieceStatus.UNACCOUNTED){		
			params.put(dm.dateEcheanceKey(), dateEcheance == null ? null : java.sql.Date.valueOf(dateEcheance));
			
			if(tiers.isNone())
				params.put(dm.tiersIdKey(), type().module().tiers().defaultTiers().id());
			else
				params.put(dm.tiersIdKey(), tiers.id());
		}
				
		ds.set(params);	
	}

	@Override
	public void reconcile() throws IOException {
		// tentative d'exécution des tâches préliminaires de lettrage 	
		if(isInvoiceOrPayment()){
			// 1 - rechercher le compte du tiers concerné
			Account auxilairyAccount = new AccountNone();
			for (Line line : type().module().lines().of(this).all()) {
				if(!line.auxiliaryAccount().isNone())
				{
					auxilairyAccount = line.auxiliaryAccount();
					break;
				}
			}
			
			if(auxilairyAccount.isNone()) // compte tiers non géré pour la facture ou le règlement
				return; // abondonner
			
			Compta module = type().module();
			
			// 2 - recherche toutes les factures et règlements comptabilisés non lettrés ayant la même référence et/ou origine pour ce compte tiers
			String reference;
			if(nature() == PieceNature.INVOICE)
				reference = reference();
			else
				reference = origin();
					
			if(StringUtils.isBlank(reference))
				return; // abandonner
			
			List<Line> invoiceLines = module.lines().ofAuxiliaryAccount(auxilairyAccount).of(PieceStatus.ACCOUNTED).of(PieceNature.INVOICE).of(LineStatus.UNLETTRATED).withReference(reference).all();
			List<Line> paymentLines = module.lines().ofAuxiliaryAccount(auxilairyAccount).of(PieceStatus.ACCOUNTED).of(PieceNature.PAYMENT).of(LineStatus.UNLETTRATED).withOrigin(reference).all();			
			
			List<Line> allLines = new ArrayList<Line>();
			allLines.addAll(invoiceLines);
			allLines.addAll(paymentLines);
			
			if(allLines.size() <= 1) // une seule occurence trouvée
				return; // abandonner la mise en correspondance
			
			// rechercher le noeud de correspondance
			Reconcile reconcile = new ReconcileNone();
			
			for (Line line : allLines) {
				if(!line.reconcile().isNone())
				{
					reconcile = line.reconcile();
					break;
				}
			}
			
			if(reconcile.isNone()){
				// créer un noeud de correspondance
				reconcile = module.reconciles().of(auxilairyAccount).add();
			}
			
			// démarrer la correspondance						
			for (Line line : allLines) {
				reconcile.add(line);					
			}
		}
	}
	
	private void validateSoldeCrediteur(Account account) throws IOException {
		if(account.isNone() || !account.refuseCreditBalance())
			return;
		
		double balance = account.lines().of(PieceStatus.ACCOUNTED).of(exercise().period()).balance();
		
		if(balance < 0)
			throw new IllegalArgumentException(String.format("Le compte %s n'admet pas de solde créditeur !", account.name()));
	}
	
	private void validateSoldeDebiteur(Account account) throws IOException {
		if(account.isNone() || !account.refuseDebitBalance())
			return;
		
		double balance = account.lines().of(PieceStatus.ACCOUNTED).of(exercise().period()).balance();
		
		if(balance > 0)
			throw new IllegalArgumentException(String.format("Le compte %s n'admet pas de solde débiteur !", account.name()));
	}
	
	@Override
	public void count() throws IOException {
		
		validate(date(), reference(), type(), dateEcheance());		
		
		for (PieceArticle article : articles().all()) {			
			for (Flux flux : article.fluxes().all()) {
				for (Line line : flux.lines().all()) {	
					
					Account generalAccount = line.generalAccount();
					validateSoldeCrediteur(generalAccount);
					validateSoldeDebiteur(generalAccount);
					
					Account auxiliaryAccount = line.auxiliaryAccount();
					validateSoldeCrediteur(auxiliaryAccount);
					validateSoldeDebiteur(auxiliaryAccount);
				}
			}		
		}
		
		ds.set(dm.statusKey(), PieceStatus.ACCOUNTED.id());
				
		reconcile(); // correspondance automatique facture - règlement
	}

	@Override
	public Exercise exercise() throws IOException {
		return type().module().exercises().get(date());
	}

	@Override
	public String origin() throws IOException {
		return ds.get(dm.originKey());
	}

	@Override
	public boolean isInvoiceOrPayment() throws IOException {
		PieceNature nature = nature();
		return nature == PieceNature.INVOICE || nature == PieceNature.PAYMENT;
	}

	@Override
	public PieceNature nature() throws IOException {
		return type().nature();
	}

	@Override
	public void validate() throws IOException {		
		articles().validate();
	}

	@Override
	public PieceArticles articles() throws IOException {
		return new PieceArticlesDb(base, this, module);
	}

	@Override
	public LocalDate dateEcheance() throws IOException {
		java.sql.Date date = ds.get(dm.dateEcheanceKey());

		if(date == null)
			return null;
		
		return date.toLocalDate();
	}

	@Override
	public Tiers tiers() throws IOException {
		UUID tiersId = ds.get(dm.tiersIdKey());
		return this.type().module().tiers().build(tiersId);
	}	
}
