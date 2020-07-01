package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Account;
import com.compta.domains.api.AccountChart;
import com.compta.domains.api.AccountChartMetadata;
import com.compta.domains.api.AccountMetadata;
import com.compta.domains.api.Accounts;
import com.compta.domains.api.Balance;
import com.compta.domains.api.BalanceAgeeDesTiers;
import com.compta.domains.api.Compta;
import com.compta.domains.api.ComptaInterfacage;
import com.compta.domains.api.Exercise.ExerciseStatus;
import com.compta.interfacage.sales.api.InvoiceInterfaceMetadata;
import com.compta.interfacage.sales.api.PaymentModeInterfaceMetadata;
import com.compta.interfacage.sales.api.PdvPaymentModeInterfaceMetadata;
import com.compta.interfacage.sales.api.ProductCategoryInterfaceMetadata;
import com.compta.domains.api.ExerciseMetadata;
import com.compta.domains.api.Exercises;
import com.compta.domains.api.FluxMetadata;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalMetadata;
import com.compta.domains.api.JournalPieceTypeMetadata;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.Journals;
import com.compta.domains.api.LineMetadata;
import com.compta.domains.api.LineStatus;
import com.compta.domains.api.Lines;
import com.compta.domains.api.OperationSens;
import com.compta.domains.api.PieceArticleMetadata;
import com.compta.domains.api.PieceMetadata;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.TiersTypes;
import com.compta.domains.api.Tierss;
import com.compta.domains.api.Trame;
import com.compta.domains.api.TrameFlux;
import com.compta.domains.api.TrameFluxDetailMetadata;
import com.compta.domains.api.TrameFluxMetadata;
import com.compta.domains.api.TrameMetadata;
import com.compta.domains.api.PieceStatus;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.PieceTypeMetadata;
import com.compta.domains.api.PieceTypeUsageCode;
import com.compta.domains.api.PieceTypes;
import com.compta.domains.api.Pieces;
import com.compta.domains.api.ReconcileMetadata;
import com.compta.domains.api.ReconcileStatus;
import com.compta.domains.api.Reconciles;
import com.compta.domains.api.TiersAccountMetadata;
import com.compta.domains.api.TiersCode;
import com.compta.domains.api.TiersType;
import com.compta.domains.api.TiersTypeMetadata;
import com.infrastructure.core.DomainMetadata;
import com.infrastructure.core.EntityBase;
import com.infrastructure.core.Period;
import com.infrastructure.core.impl.PeriodNone;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.securities.api.Company;
import com.securities.api.Contacts;
import com.securities.api.Feature;
import com.securities.api.FeatureSubscribed;
import com.securities.api.Features;
import com.securities.api.Indicators;
import com.securities.api.Log;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.securities.api.PaymentModeStatus;
import com.securities.api.PaymentModes;
import com.securities.api.Sequence;
import com.securities.api.Sequences;
import com.securities.api.Taxes;
import com.securities.api.User;
import com.securities.impl.ModuleNone;

public final class ComptaDb extends EntityBase<Compta, UUID> implements Compta {

	private final transient Base base;
	private final transient Module origin;
	
	public ComptaDb(final Base base, final Module module){
		super(module.id());
		this.base = base;
		this.origin = module;
	}
	
	@Override
	public Company company() throws IOException {
		return origin.company();
	}

	@Override
	public String description() throws IOException {
		return origin.description();
	}

	@Override
	public Module install() throws IOException {
		Module module = origin.install();
		
		Company company = company();
		User adminUser = company.moduleAdmin().membership().defaultUser();
		
		// créer le plan comptable sur 6 chiffres
		UUID chartId = UUID.randomUUID();
		int codeDigits = 6;
		base.executeUpdate("insert into compta.account_charts (id, name, code_digits, moduleid, exo_last_month, exo_last_day, journalran_id, datecreated, ownerid, lastmodifieddate, lastmodifierid, owner_companyid)"
				+ "("
				+ "select ?, name, ?, ?, 12, 31, null, now(), ?, now(), ?, ? from compta.account_chart_templates"
				+ ")", Arrays.asList(chartId, codeDigits, id, adminUser.id(), adminUser.id(), company().id()));
		
		base.executeUpdate("insert into compta.accounts (id, code, name, typeid, chartid, is_auxiliary, refuse_credit_balance, refuse_debit_balance, datecreated, ownerid, lastmodifieddate, lastmodifierid, owner_companyid)"
				+ "("
				+ "select uuid_in((md5((random())::text))::cstring), rpad(code, ?, '0'), name, typeid, ?, false, refuse_credit_balance, refuse_debit_balance, now(), ?, now(), ?, ? from compta.account_templates	"
				+ ")", Arrays.asList(codeDigits, chartId, adminUser.id(), adminUser.id(), company.id()));
		
		Accounts accounts = accounts();
		
		// 1 - type de tiers
		TiersTypes tiersTypes = tiersTypes();
		
		// 1 - 1 - tiers fournisseur
		Sequence sequenceSupplierAcpt = sequences().add("Séquence compte tiers Fournisseur", "401F", StringUtils.EMPTY, 0, 1, 1);
		Sequence sequenceSupplierLettrageAcpt = sequences().add("Séquence lettrage compte tiers Fournisseur", "B", StringUtils.EMPTY, 0, 1, 1);
		TiersType supplierTiersType = tiersTypes.add("Fournisseur", TiersCode.FOURNISSEUR, accounts.get("401000"), sequenceSupplierAcpt, sequenceSupplierLettrageAcpt);
		
		// 1 - 2 - tiers client
		Sequence sequenceCustomerAcpt = sequences().add("Séquence compte tiers Client", "411C", StringUtils.EMPTY, 0, 1, 1);
		Sequence sequenceCustomerLettrageAcpt = sequences().add("Séquence lettrage compte tiers Client", "A", StringUtils.EMPTY, 0, 1, 1);
		TiersType customerTiersType = tiersTypes.add("Client", TiersCode.CUSTOMER, accounts.get("411000"), sequenceCustomerAcpt, sequenceCustomerLettrageAcpt);
				
		// 2 - type de pièce
		PieceTypes pieceTypes = pieceTypes();
				
		// 2 - 1 - facture d'achats
		Sequence sequenceSupplierInvoiceReference = sequences().add("Séquence facture Fournisseur", "FACTU/{year}/", StringUtils.EMPTY, 5, 1, 1);
		PieceType supplierInvoicePieceType = pieceTypes.add("Facture d'achats", PieceTypeUsageCode.SELLER_INVOICE, supplierTiersType, PieceNature.INVOICE, sequenceSupplierInvoiceReference, StringUtils.EMPTY);
		
		// 2 - 1 - 1 - modèle saisie 
		Trame supplierInvoiceTrame = supplierInvoicePieceType.trames().add("Modèle par défaut");
		TrameFlux supplierInvoiceTrameFlux = supplierInvoiceTrame.fluxes().add("Réception de la facture fournisseur N°{reference}", JournalType.ACHATS, new JournalNone());
		supplierInvoiceTrameFlux.details().add(accounts.get("601000"), OperationSens.DEBIT, true, "{base}");
		supplierInvoiceTrameFlux.details().add(accounts.get("445200"), OperationSens.DEBIT, false, "{base}*{tva}");
		supplierInvoiceTrameFlux.details().add(accounts.get("401000"), OperationSens.CREDIT, false, "{base}+{base}*{tva}");
		supplierInvoiceTrameFlux.validate();
				
		supplierInvoicePieceType.changePreferredTrame(supplierInvoiceTrame);
		
		// 2 - 2 - facture de ventes
		Sequence sequenceCustomerInvoiceReference = sequences().add("Séquence facture Client", "FAC/{year}/", StringUtils.EMPTY, 5, 1, 1);
		PieceType customerInvoicePieceType = pieceTypes.add("Facture de ventes", PieceTypeUsageCode.CUSTOMER_INVOICE, customerTiersType, PieceNature.INVOICE, sequenceCustomerInvoiceReference, StringUtils.EMPTY);
		
		// 2 - 2 - 1 - modèle de saisie produit fini
		Trame customerInvoiceProduitFiniTrame = customerInvoicePieceType.trames().add("Vente de produits finis");
		TrameFlux customerInvoiceProduitFiniTrameFlux = customerInvoiceProduitFiniTrame.fluxes().add("Facture client N°{reference} pour vente de produits finis", JournalType.VENTE, new JournalNone());
		customerInvoiceProduitFiniTrameFlux.details().add(accounts.get("702000"), OperationSens.CREDIT, false, "{base}");
		customerInvoiceProduitFiniTrameFlux.details().add(accounts.get("443100"), OperationSens.CREDIT, false, "{base}*{tva}");
		customerInvoiceProduitFiniTrameFlux.details().add(accounts.get("411000"), OperationSens.DEBIT, false, "{base}+{base}*{tva}");
		customerInvoiceProduitFiniTrameFlux.validate();
		
		// 2 - 2 - 2 modèle de saisie prestation de service
		Trame customerInvoicePrestationServiceTrame = customerInvoicePieceType.trames().add("Vente de prestations de service");
		TrameFlux customerInvoicePrestationServiceTrameFlux = customerInvoicePrestationServiceTrame.fluxes().add("Facture client N°{reference} pour prestation de service", JournalType.VENTE, new JournalNone());
		customerInvoicePrestationServiceTrameFlux.details().add(accounts.get("706000"), OperationSens.CREDIT, false, "{base}");
		customerInvoicePrestationServiceTrameFlux.details().add(accounts.get("443200"), OperationSens.CREDIT, false, "{base}*{tva}");
		customerInvoicePrestationServiceTrameFlux.details().add(accounts.get("411000"), OperationSens.DEBIT, false, "{base}+{base}*{tva}");
		customerInvoicePrestationServiceTrameFlux.validate();
		
		// 2 - 2 - 3 modèle de saisie marchandises
		Trame customerInvoiceMarchandiseTrame = customerInvoicePieceType.trames().add("Vente de marchandises");
		TrameFlux customerInvoiceMarchandiseTrameFlux = customerInvoiceMarchandiseTrame.fluxes().add("Facture client N°{reference} pour vente de marchandises", JournalType.VENTE, new JournalNone());
		customerInvoiceMarchandiseTrameFlux.details().add(accounts.get("701000"), OperationSens.CREDIT, false, "{base}");
		customerInvoiceMarchandiseTrameFlux.details().add(accounts.get("443100"), OperationSens.CREDIT, false, "{base}*{tva}");
		customerInvoiceMarchandiseTrameFlux.details().add(accounts.get("411000"), OperationSens.DEBIT, false, "{base}+{base}*{tva}");
		customerInvoiceMarchandiseTrameFlux.validate();
				
		customerInvoicePieceType.changePreferredTrame(customerInvoiceMarchandiseTrame);
		
		Sequence sequencePaymentReference = sequences().add("Séquence Paiement", "PAY/{year}/", StringUtils.EMPTY, 8, 1, 1);
		
		// 2 - 4 - Décaissements		
		PieceType decaissementPieceType = pieceTypes.add("Décaissement", PieceTypeUsageCode.SELLER_PAYMENT, supplierTiersType, PieceNature.PAYMENT, sequencePaymentReference, StringUtils.EMPTY);
		
		// 2 - 4 - 1 - modèle de saisie décaissement bancaire
		Trame decaissementBankTrame = decaissementPieceType.trames().add("Décaissement bancaire");
		TrameFlux decaissementBankTrameFlux = decaissementBankTrame.fluxes().add("Règlement bancaire N°{reference}", JournalType.BANQUE, new JournalNone());
		decaissementBankTrameFlux.details().add(accounts.get("521000"), OperationSens.CREDIT, true, "{base}");
		decaissementBankTrameFlux.details().add(accounts.get("401000"), OperationSens.DEBIT, false, "{base}");
		decaissementBankTrameFlux.validate();
			
		// 2 - 4 - 2 - modèle de saisie décaissement en espèces
		Trame decaissementCashTrame = decaissementPieceType.trames().add("Décaissement en espèces");
		TrameFlux decaissementCashTrameFlux = decaissementCashTrame.fluxes().add("Règlement en espèces N°{reference}", JournalType.LIQUIDITES, new JournalNone());
		decaissementCashTrameFlux.details().add(accounts.get("571000"), OperationSens.CREDIT, true, "{base}");
		decaissementCashTrameFlux.details().add(accounts.get("401000"), OperationSens.DEBIT, false, "{base}");
		decaissementCashTrameFlux.validate();
				
		decaissementPieceType.changePreferredTrame(decaissementBankTrame);
		
		// 2 - 5 - Encaissements
		PieceType encaissementPieceType = pieceTypes.add("Encaissement", PieceTypeUsageCode.CUSTOMER_PAYMENT, customerTiersType, PieceNature.PAYMENT, sequencePaymentReference, StringUtils.EMPTY);
				
		// 2 - 5 - 1 - modèle de saisie encaissement bancaire
		Trame encaissementBankTrame = encaissementPieceType.trames().add("Encaissement bancaire");
		TrameFlux encaissementBankTrameFlux = encaissementBankTrame.fluxes().add("Règlement bancaire N°{reference}", JournalType.BANQUE, new JournalNone());
		encaissementBankTrameFlux.details().add(accounts.get("521000"), OperationSens.DEBIT, true, "{base}");
		encaissementBankTrameFlux.details().add(accounts.get("411000"), OperationSens.CREDIT, false, "{base}");
		encaissementBankTrameFlux.validate();
					
		// 2 - 5 - 2 - modèle de saisie encaissement en espèces
		Trame encaissementCashTrame = encaissementPieceType.trames().add("Encaissement en espèces");
		TrameFlux encaissementCashTrameFlux = encaissementCashTrame.fluxes().add("Règlement en espèces N°{reference}", JournalType.LIQUIDITES, new JournalNone());
		encaissementCashTrameFlux.details().add(accounts.get("571000"), OperationSens.DEBIT, true, "{base}");
		encaissementCashTrameFlux.details().add(accounts.get("411000"), OperationSens.CREDIT, false, "{base}");
		encaissementCashTrameFlux.validate();
				
		encaissementPieceType.changePreferredTrame(encaissementBankTrame);
		
		// 2 - 6 - Divers
		Sequence sequenceDiversReference = sequences().add("Séquence Opérations Diverses", "OD/{year}/", StringUtils.EMPTY, 8, 1, 1);
		PieceType diversPieceType = pieceTypes.add("Divers", PieceTypeUsageCode.USER_USAGE, new TiersTypeNone(), PieceNature.OTHER, sequenceDiversReference, StringUtils.EMPTY);
				
		// 3 - journaux comptables
		Journals journals = journals();	
		
		// 3 - 1 - journal de banque			
		Journal jrnlBank = journals.add("BNK1", "Banque", JournalType.BANQUE, accounts.get("521100"), true);
		jrnlBank.viewOnDashboard(true);
		
		jrnlBank.addPieceType(encaissementPieceType);
		jrnlBank.addPieceType(decaissementPieceType);
		
		// 3 - 2 - journal de caisse			
		Journal jrnlCash = journals.add("CSH1", "Caisse", JournalType.LIQUIDITES, accounts.get("571000"), true);
		jrnlCash.viewOnDashboard(true);
				
		jrnlCash.addPieceType(encaissementPieceType);
		jrnlCash.addPieceType(decaissementPieceType);
				
		// 3 - 3 - journal d'achats			
		Journal jrnlAchats = journals.add("FACTU", "Journal d'achats", JournalType.ACHATS, accounts.get("601000"), true);
		jrnlAchats.viewOnDashboard(true);
				
		jrnlAchats.addPieceType(supplierInvoicePieceType);

		// 3 - 4 - journal de ventes		
		Journal jrnlVente = journals.add("FAC", "Journal de ventes", JournalType.VENTE, accounts.get("701000"), false);
		jrnlVente.viewOnDashboard(true);
				
		jrnlVente.addPieceType(customerInvoicePieceType);
		
		// 3 - 5 - journal RAN		
		Journal jrnlRan = journals.add("RAN1", "Journal des Reports-A-Nouveau", JournalType.DIVERS, new AccountNone(), false);
				
		jrnlRan.addPieceType(diversPieceType);
		updateJournalRan(jrnlRan);
				
		// 3 - 6 - journal Opérations diverses		
		Journal jrnlOD = journals.add("OD1", "Opérations diverses", JournalType.DIVERS, new AccountNone(), false);
				
		jrnlOD.addPieceType(diversPieceType);
								
		return new ComptaDb(base, module);
	}

	@Override
	public boolean isInstalled() {
		return origin.isInstalled();
	}

	@Override
	public boolean isSubscribed() {
		return origin.isSubscribed();
	}

	@Override
	public String name() throws IOException {
		return origin.name();
	}

	@Override
	public int order() throws IOException {
		return origin.order();
	}

	@Override
	public String shortName() throws IOException {
		return origin.shortName();
	}

	@Override
	public ModuleType type() throws IOException {
		return origin.type();
	}

	@Override
	public Module uninstall() throws IOException {
		
		// rendre les types de pièces indépendantes des modèles de saisies
		for (PieceType pieceType : pieceTypes().all()) {
			pieceType.changePreferredTrame(new TrameNone());
		}
		
		updateJournalRan(new JournalNone());
		
		List<DomainMetadata> domains = 
				Arrays.asList(
					InvoiceInterfaceMetadata.create(),
					PaymentModeInterfaceMetadata.create(),										
					PdvPaymentModeInterfaceMetadata.create(),
					ProductCategoryInterfaceMetadata.create(),
					LineMetadata.create(),
					ReconcileMetadata.create(),
					FluxMetadata.create(),
					PieceArticleMetadata.create(),
					PieceMetadata.create(),
					ExerciseMetadata.create(),
					TrameFluxDetailMetadata.create(),
					TrameFluxMetadata.create(),
					TrameMetadata.create(),
					JournalPieceTypeMetadata.create(),
					PieceTypeMetadata.create(),
					JournalMetadata.create(),
					TiersTypeMetadata.create(),
					TiersAccountMetadata.create(),
					AccountMetadata.create(),
					AccountChartMetadata.create()
				);		
		
		for (DomainMetadata domainMetadata : domains) {
			base.deleteAll(domainMetadata); 
		}
		
		List<String> sequenceNames = Arrays.asList( "Séquence compte tiers Fournisseur", 
													"Séquence lettrage compte tiers Fournisseur", 
													"Séquence compte tiers Client", 
													"Séquence lettrage compte tiers Client", 
													"Séquence facture Fournisseur", 
													"Séquence facture Client",
													"Séquence Paiement",
													"Séquence Opérations Diverses");
				
		Sequences sequences = sequences();
		for (String name : sequenceNames) {
			List<Sequence> list = sequences().find(name);			
			
			if(!list.isEmpty()){
				for (Sequence seq : list) {				
					try {
						sequences.delete(seq);
					} catch (Exception ignore) {}					
				}
			}
		}
		
		// finaliser
		Module module = origin.uninstall();
		return new ComptaDb(base, module);
	}

	@Override
	public AccountChart chart() throws IOException {
		AccountChartMetadata accDm = AccountChartMetadata.create();
		
		String statement = String.format("SELECT %s FROM %s WHERE %s=?", accDm.keyName(), accDm.domainName(), accDm.moduleIdKey());		
		List<Object> results = base.executeQuery(statement, Arrays.asList(origin.id()));
		
		if(results.isEmpty()) {
			throw new IllegalArgumentException("Le plan comptable n'est pas configuré !");
		}else{
			return new AccountChartDb(base, UUIDConvert.fromObject(results.get(0)), this);
		}		
	}

	@Override
	public Journals journals() throws IOException {
		return new JournalsDb(base, this, JournalType.NONE);
	}

	@Override
	public void activate(boolean active) throws IOException {
		origin.activate(active);
	}

	@Override
	public boolean isActive() {
		return origin.isActive();
	}

	@Override
	public Accounts accounts() throws IOException {
		return chart().accounts();
	}

	@Override
	public PieceTypes pieceTypes() throws IOException {
		return new PieceTypesDb(base, this, new JournalNone());
	}

	@Override
	public TiersTypes tiersTypes() throws IOException {
		return new TiersTypesDb(base, this);
	}

	@Override
	public Sequences sequences() throws IOException {
		return company().moduleAdmin().sequences();
	}

	@Override
	public Pieces pieces() throws IOException {
		return new PiecesDb(base, this, new PieceTypeNone(), PieceStatus.NONE, new PeriodNone(), StringUtils.EMPTY, new ModuleNone());
	}

	@Override
	public Contacts contacts() throws IOException {
		return company().moduleAdmin().contacts();
	}

	@Override
	public Tierss tiers() throws IOException {
		return new TierssImpl(this);
	}

	@Override
	public Exercises exercises() throws IOException {
		return new ExercisesDb(base, this, ExerciseStatus.NONE, null);
	}

	@Override
	public int lastMonthExo() throws IOException {
		AccountChartMetadata dm = AccountChartMetadata.create();
		DomainStore ds = base.domainsStore(dm).createDs(chart().id());
		
		return ds.get(dm.lastMonthExoKey());
	}

	@Override
	public int lastDayExo() throws IOException {
		AccountChartMetadata dm = AccountChartMetadata.create();
		DomainStore ds = base.domainsStore(dm).createDs(chart().id());
		
		return ds.get(dm.lastDayExoKey());
	}

	@Override
	public void updateLastDayExo(int month, int day) throws IOException {
		
		if(!(month >=1 && month<=12))
			throw new IllegalArgumentException("Mois invalide !");
		
		if(!(day >= 1 && day <= 31))
			throw new IllegalArgumentException("Jour du mois invalide !");
		
		AccountChartMetadata dm = AccountChartMetadata.create();
		DomainStore ds = base.domainsStore(dm).createDs(chart().id());
		
		ds.set(dm.lastDayExoKey(), day);
		ds.set(dm.lastMonthExoKey(), month);
	}

	@Override
	public PieceTypes pieceTypes(Journal journal) throws IOException {
		return new PieceTypesDb(base, this, journal);
	}

	@Override
	public Lines lines() throws IOException {
		return new LinesDb(base, this, new JournalNone(), new PieceNone(), PieceStatus.NONE, new PeriodNone(), PieceNature.NONE, LineStatus.NONE, StringUtils.EMPTY, StringUtils.EMPTY, new AccountNone(), new AccountNone(), new TiersTypeNone(), new FluxNone());
	}

	@Override
	public Journal journalRan() throws IOException {
		AccountChartMetadata dm = AccountChartMetadata.create();
		DomainStore ds = base.domainsStore(dm).createDs(chart().id());
		
		UUID journalId = ds.get(dm.journalRanIdKey());
		return journals().build(journalId);
	}

	@Override
	public void updateJournalRan(Journal journal) throws IOException {
		
		if(journal.type() != JournalType.DIVERS)
			throw new IllegalArgumentException("Le journal doit être un journal de type opérations diverses !");
		
		AccountChartMetadata dm = AccountChartMetadata.create();
		DomainStore ds = base.domainsStore(dm).createDs(chart().id());
		
		ds.set(dm.journalRanIdKey(), journal.id());
	}

	@Override
	public Reconciles reconciles() throws IOException {
		return new ReconcilesDb(base, this, new AccountNone(), new TiersTypeNone(), ReconcileStatus.NONE);
	}
	
	@Override
	public Balance balanceGenerale(Period period) throws IOException {
		return new BalanceGenerale(base, this, period);
	}

	@Override
	public Balance balanceAuxiliaire(TiersType tiersType, Period period) throws IOException {
		return new BalanceAuxiliaire(base, this, period, tiersType);
	}

	@Override
	public BalanceAgeeDesTiers balanceAgeeDesTiers(TiersType tiersType, Account auxiliaryAccount, LocalDate startDate) throws IOException {
		return new BalanceAgeeDesTiersDb(base, this, auxiliaryAccount, tiersType, startDate);
	}

	@Override
	public Taxes taxes() throws IOException {
		return company().moduleAdmin().taxes();
	}

	@Override
	public ComptaInterfacage interfacage() throws IOException {
		return new ComptaInterfacageDb(base, this);
	}

	@Override
	public PaymentModes paymentModes() throws IOException {
		return company().moduleAdmin().paymentModes().of(PaymentModeStatus.ENABLED);
	}
	
	@Override
	public Features featuresAvailable() throws IOException {
		return origin.featuresAvailable();
	}

	@Override
	public Features featuresSubscribed() throws IOException {
		return origin.featuresSubscribed();
	}

	@Override
	public Indicators indicators() throws IOException {
		return origin.indicators();
	}

	@Override
	public Module subscribe() throws IOException {
		return origin.subscribe();
	}

	@Override
	public FeatureSubscribed subscribeTo(Feature feature) throws IOException {
		return origin.subscribeTo(feature);
	}

	@Override
	public Module unsubscribe() throws IOException {
		return origin.unsubscribe();
	}

	@Override
	public void unsubscribeTo(Feature feature) throws IOException {
		origin.unsubscribeTo(feature);
	}

	@Override
	public Features featuresProposed() throws IOException {
		return origin.featuresProposed();
	}

	@Override
	public Log log() throws IOException {
		return origin.log();
	}
}
