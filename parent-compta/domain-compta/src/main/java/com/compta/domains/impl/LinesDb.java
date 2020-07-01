package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Flux;
import com.compta.domains.api.FluxMetadata;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalMetadata;
import com.compta.domains.api.LineStatus;
import com.compta.domains.api.Line;
import com.compta.domains.api.LineMetadata;
import com.compta.domains.api.Lines;
import com.compta.domains.api.Piece;
import com.compta.domains.api.PieceArticleMetadata;
import com.compta.domains.api.PieceMetadata;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.PieceStatus;
import com.compta.domains.api.PieceTypeMetadata;
import com.compta.domains.api.Tiers;
import com.compta.domains.api.TiersAccountMetadata;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.core.Period;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;

public class LinesDb extends GuidKeyAdvancedQueryableDb<Line, LineMetadata> implements Lines {

	private final transient Compta module;
	private final transient PieceStatus pieceStatus;
	private final transient Journal journal;
	private final transient Period pieceDatePeriod;
	private final transient Piece piece;
	private final transient PieceNature pieceNature;
	private final transient LineStatus status;
	private final transient String reference;
	private final transient String origin;	
	private final transient Account generalAccount;
	private final transient Account auxiliaryAccount;
	private final transient TiersType tiersType;
	private final transient Flux flux;
	
	public LinesDb(final Base base, final Compta module, final Journal journal, final Piece piece, final PieceStatus pieceStatus, final Period pieceDatePeriod, final PieceNature pieceNature, final LineStatus status, final String reference, final String origin, final Account generalAccount, final Account auxiliaryAccount, final TiersType tiersType, final Flux flux){
		super(base, "Ecriture introuvable !");	
		this.module = module;
		this.journal = journal;
		this.pieceStatus = pieceStatus;
		this.pieceDatePeriod = pieceDatePeriod;
		this.piece = piece;
		this.pieceNature = pieceNature;
		this.status = status;
		this.reference = reference;
		this.origin = origin;
		this.generalAccount = generalAccount;
		this.auxiliaryAccount = auxiliaryAccount;
		this.tiersType = tiersType;
		this.flux = flux;
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		
		JournalMetadata jnlDm = JournalMetadata.create();
		PieceMetadata pDm = PieceMetadata.create();
		PieceTypeMetadata ptDm = PieceTypeMetadata.create();
		FluxMetadata flDm = FluxMetadata.create();
		PieceArticleMetadata paDm = PieceArticleMetadata.create();
		
		String statement = String.format("%s ln "
				+ "{statement-tiers-compte} "
				+ "JOIN %s fl ON fl.%s=ln.%s "
				+ "left JOIN %s pa ON pa.%s = fl.%s "
				+ "left JOIN %s p ON p.%s = pa.%s "
				+ "left JOIN %s pt ON pt.%s = p.%s "
				+ "left JOIN %s jnl ON jnl.%s = fl.%s "
				+ "WHERE (p.%s ILIKE ? OR fl.%s ILIKE ? OR p.%s ILIKE ?) AND jnl.%s=?",				 
				dm.domainName(), 
				flDm.domainName(), flDm.keyName(), dm.fluxIdKey(),
				paDm.domainName(), paDm.keyName(), flDm.articleIdKey(),
				pDm.domainName(), pDm.keyName(), paDm.pieceIdKey(),
				ptDm.domainName(), ptDm.keyName(), pDm.typeIdKey(),
				jnlDm.domainName(), jnlDm.keyName(), flDm.journalIdKey(),
				pDm.referenceKey(), flDm.objectKey(), pDm.originKey(), jnlDm.moduleIdKey());
		
		params.add(filter + "%");
		params.add("%" + filter + "%");
		params.add(filter + "%");
		params.add(module.id());
		
		if(!journal.isNone()){
			statement = String.format("%s AND jnl.%s=?", statement, jnlDm.keyName());
			params.add(journal.id());
		}
		
		if(!piece.isNone()){
			statement = String.format("%s AND p.%s=?", statement, pDm.keyName());
			params.add(piece.id());
		}
		
		if(pieceStatus != PieceStatus.NONE){
			statement = String.format("%s AND p.%s=?", statement, pDm.statusKey());
			params.add(pieceStatus.id());
		}
		
		if(pieceDatePeriod.isDefined()){
			statement = String.format("%s AND p.%s BETWEEN ? AND ?", statement, pDm.pieceDateKey());
			params.add(java.sql.Date.valueOf(pieceDatePeriod.start()));
			params.add(java.sql.Date.valueOf(pieceDatePeriod.end()));
		}
		
		if(pieceNature != PieceNature.NONE){
			statement = String.format("%s AND pt.%s=?", statement, ptDm.natureIdKey());
			params.add(pieceNature.id());
		}
		
		if(status != LineStatus.NONE){
			
			if(status == LineStatus.UNRECONCILED)
				statement = String.format("%s AND ln.%s IS NULL", statement, dm.reconcileIdKey());
			
			if(status == LineStatus.RECONCILED)
				statement = String.format("%s AND ln.%s IS NOT NULL", statement, dm.reconcileIdKey());
			
			if(status == LineStatus.UNLETTRATED)
				statement = String.format("%s AND ln.%s IS NULL", statement, dm.lettrageKey());
			
			if(status == LineStatus.LETTRATED)
				statement = String.format("%s AND ln.%s IS NOT NULL", statement, dm.lettrageKey());
		}
		
		if(!StringUtils.isBlank(reference)){
			statement = String.format("%s AND p.%s ILIKE ?", statement, pDm.referenceKey());
			params.add(reference);
		}
		
		if(!StringUtils.isBlank(origin)){
			statement = String.format("%s AND p.%s ILIKE ?", statement, pDm.originKey());
			params.add(origin);
		}
		
		if(!generalAccount.isNone()) {
			statement = String.format("%s AND ln.%s=?", statement, dm.generalAccountIdKey());
			params.add(generalAccount.id());
		}
		
		if(!auxiliaryAccount.isNone()) {
			statement = String.format("%s AND ln.%s=?", statement, dm.auxiliaryAccountIdKey());
			params.add(auxiliaryAccount.id());
		}
		
		if(!tiersType.isNone()) {
			TiersAccountMetadata tcptDm = TiersAccountMetadata.create();			
			String statementJoin = String.format("JOIN %s tcpt on tcpt.%s=ln.%s", tcptDm.domainName(), tcptDm.keyName(), dm.auxiliaryAccountIdKey());
			statement = statement.replace("{statement-tiers-compte}", statementJoin);
			
			statement = String.format("%s AND tcpt.%s=?", statement, tcptDm.tiersTypeIdKey());
			params.add(tiersType.id());
		}else{
			statement = statement.replace("{statement-tiers-compte}", "");
		}
		
		if(!flux.isNone()) {
			statement = String.format("%s AND fl.%s=?", statement, flDm.keyName());
			params.add(flux.id());
		}
		
		String orderClause;
		
		if(!piece.isNone() || !flux.isNone())
			orderClause = String.format("ORDER BY ln.%s ASC", dm.orderKey());	
		else
			orderClause = String.format("ORDER BY p.%s, ln.%s DESC", pDm.pieceDateKey(), dm.orderKey());
				
		String keyResult = String.format("ln.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	public Line add(Tiers tiers, Account generalAccount, double debit, double credit) throws IOException {
		
		Piece piece = flux.article().piece();
		if(piece.status() == PieceStatus.ACCOUNTED)
			throw new IllegalArgumentException("Vous ne pouvez pas modifier les écritures d'une pièce comptabilisée !");
		
		validate(generalAccount, debit, credit, piece, flux);
		
		Map<String, Object> params = new HashMap<String, Object>();
				
		TiersType tiersType = piece.type().tiersTypeManaged();

		if(!tiersType.isNone() && tiersType.generalAccount().equals(generalAccount)) // s'agit-il d'un compte de tiers ?
		{
			if(tiers.isNone()) // le tiers est-il renseigné ?
				tiers = module.tiers().defaultTiers(); // sinon prendre le tiers anonyme
			
			// 1 - récupérer le compte du tiers s'il existe
			Account auxiliaryAccount = tiers.accounts().getAuxiliaryAccountOrDefault(tiersType);
			
			if(auxiliaryAccount.isNone()) // s'il n'existe pas, créer un compte
				auxiliaryAccount = tiers.accounts().addAuxiliaryAccount(tiersType.sequence().generate(), tiers.name(), tiersType.generalAccount().type(), tiersType);
			
			params.put(dm.auxiliaryAccountIdKey(), auxiliaryAccount.id());			
		}
		
		// 2 - enregistrer
		params.put(dm.generalAccountIdKey(), generalAccount.id());		
		params.put(dm.debitKey(), debit);
		params.put(dm.creditKey(), credit);
		params.put(dm.fluxIdKey(), flux.id());
		params.put(dm.orderKey(), all().size() + 1);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}
	
	public static void validate(Account generalAccount, double debit, double credit, Piece piece, Flux flux) throws IOException {
		
		if(flux.isNone())
			throw new IllegalArgumentException("Vous devez spécifier le flux auquel les écritures doivent être ajoutée !");
		
		if(generalAccount.isNone())
			throw new IllegalArgumentException("Le compte général doit être spécifié !");
		
		if(generalAccount.isAuxiliary())
			throw new IllegalArgumentException("Le compte général ne peut pas être un compte auxiliaire !");
		
		if(debit == 0 && credit == 0)
			throw new IllegalArgumentException("L'une des colonnes débit ou crédit doit être au moins renseignée !");
		
		if(debit > 0 && credit > 0)
			throw new IllegalArgumentException("Les colonnes débit et crédit ne peuvent pas être renseignées pour une même écriture !");
	}

	@Override
	public void deleteAll() throws IOException {
		if(flux.isNone() || flux.article().piece().isNone())
			throw new IllegalArgumentException("Vous ne pouvez supprimer que les écritures d'une pièce comptable !");
		
		for (Line line : all()) {
			delete(line);
		}
	}
	
	@Override
	public void validate() throws IOException {
		
		Piece piece = flux.article().piece();
		
		if(piece.isNone())
			throw new IllegalArgumentException("Vous ne pouvez valider que les écritures d'une pièce !");
		
		Journal journal = flux.journal();
		
		if(journal.isNone())
			throw new IllegalArgumentException("Vous devez spécifier le journal dans lequel vous désirez passer les écritures !");
		
		double debit = 0;
		double credit = 0;
		boolean hasGeneralAccountLine = false;
		for (Line line : all()) {
			debit += line.debit();
			credit += line.credit();
			
			if(journal.account().isNone() || (!journal.account().isNone() && line.generalAccount().equals(journal.account())))
				hasGeneralAccountLine = true;
		}
		
		if(!hasGeneralAccountLine && journal.validateAccount())
			throw new IllegalArgumentException(String.format("%s : aucune écriture n'est rattaché au compte associé du journal !", journal.name()));
		
		if(debit != credit)
			throw new IllegalArgumentException(String.format("%s : les montants au débit et au crédit doivent être égaux !", journal.name()));
	}

	@Override
	public Lines of(Journal journal) throws IOException {
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, generalAccount, auxiliaryAccount, tiersType, flux);
	}

	@Override
	public Lines of(Piece piece) throws IOException {
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, generalAccount, auxiliaryAccount, tiersType, flux);
	}

	@Override
	public Lines of(PieceStatus pieceStatus) throws IOException {
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, generalAccount, auxiliaryAccount, tiersType, flux);
	}

	@Override
	public Lines of(Period pieceDatePeriod) throws IOException {
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, generalAccount, auxiliaryAccount, tiersType, flux);
	}

	@Override
	public Lines of(PieceNature pieceNature) throws IOException {
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, generalAccount, auxiliaryAccount, tiersType, flux);
	}

	@Override
	public Lines of(LineStatus status) throws IOException {
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, generalAccount, auxiliaryAccount, tiersType, flux);
	}

	@Override
	public Lines withReference(String reference) throws IOException {
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, generalAccount, auxiliaryAccount, tiersType, flux);
	}

	@Override
	public Lines withOrigin(String origin) throws IOException {
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, generalAccount, auxiliaryAccount, tiersType, flux);
	}

	@Override
	public Lines ofGeneralAccount(Account account) throws IOException {
		
		if(!account.isNone() && account.isAuxiliary())
			throw new IllegalArgumentException("Type de compte invalide : compte général attendu !");
		
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, account, auxiliaryAccount, tiersType, flux);
	}

	@Override
	public Lines ofAuxiliaryAccount(Account account) throws IOException {
		
		if(!account.isNone() && !account.isAuxiliary())
			throw new IllegalArgumentException("Type de compte invalide : compte auxiliaire attendu !");
		
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, generalAccount, account, tiersType, flux);
	}

	@Override
	public Lines of(TiersType tiersType) throws IOException {
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, generalAccount, auxiliaryAccount, tiersType, flux);
	}

	@Override
	public Lines of(Flux flux) throws IOException {
		return new LinesDb(base, module, journal, piece, pieceStatus, pieceDatePeriod, pieceNature, status, reference, origin, generalAccount, auxiliaryAccount, tiersType, flux);
	}

	@Override
	public double debit() throws IOException {
		return buildQuery(StringUtils.EMPTY).sum(dm.debitKey());
	}

	@Override
	public double credit() throws IOException {
		return buildQuery(StringUtils.EMPTY).sum(dm.creditKey());
	}

	@Override
	public double balance() throws IOException {
		return debit() - credit();
	}

	@Override
	protected Line newOne(UUID id) {
		return new LineDb(base, id, module);
	}

	@Override
	public Line none() {
		return new LineNone();
	}
}
