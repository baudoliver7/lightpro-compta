package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Piece;
import com.compta.domains.api.PieceMetadata;
import com.compta.domains.api.PieceStatus;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.PieceTypeMetadata;
import com.compta.domains.api.Pieces;
import com.compta.domains.api.Tiers;
import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.core.Period;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.securities.api.Module;

public final class PiecesDb extends GuidKeyAdvancedQueryableDb<Piece, PieceMetadata> implements Pieces {

	private final transient Compta module;
	private final transient PieceType pieceType;
	private final transient PieceStatus status;
	private final transient Period period;
	private final transient String reference;
	private final transient Module moduleOrigin;
	
	public PiecesDb(final Base base, final Compta module, final PieceType pieceType, final PieceStatus status, final Period period, final String reference, final Module moduleOrigin){
		super(base, "Pièce introuvable !");	
		this.module = module;
		this.pieceType = pieceType;
		this.status = status;
		this.period = period;
		this.reference = reference;
		this.moduleOrigin = moduleOrigin;
	}
	
	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		PieceTypeMetadata ptDm = PieceTypeMetadata.create();
		
		String statement = String.format("%s p "
				+ "JOIN %s pt ON pt.%s = p.%s "
				+ "WHERE (p.%s ILIKE ? OR p.%s ILIKE ?) AND pt.%s=?",				 
				dm.domainName(), 
				ptDm.domainName(), ptDm.keyName(), dm.typeIdKey(),
				dm.referenceKey(), dm.originKey(), ptDm.moduleIdKey());
		
		params.add(filter + "%");
		params.add(filter + "%");
		params.add(module.id());
		
		if(!pieceType.isNone()){
			statement = String.format("%s AND p.%s=?", statement, dm.typeIdKey());
			params.add(pieceType.id());
		}
		
		if(status != PieceStatus.NONE){
			statement = String.format("%s AND p.%s=?", statement, dm.statusKey());
			params.add(status.id());
		}
		
		if(period.isDefined()){
			statement = String.format("%s AND p.%s BETWEEN ? AND ?", statement, dm.pieceDateKey());
			params.add(java.sql.Date.valueOf(period.start()));
			params.add(java.sql.Date.valueOf(period.end()));
		}
		
		if(!StringUtils.isBlank(reference)){
			statement = String.format("%s AND p.%s ILIKE ?", statement, dm.referenceKey());
			params.add(reference);
		}
		
		if(!moduleOrigin.isNone()){
			statement = String.format("%s AND p.%s=?", statement, dm.originModuleIdKey());
			params.add(moduleOrigin.id());
		}
		
		String orderClause = String.format("ORDER BY p.%s DESC", dm.pieceDateKey());
		
		String keyResult = String.format("p.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	public void delete(Piece item) throws IOException {
		if(contains(item))
		{			
			if(item.exercise().isClosed())
				throw new IllegalArgumentException("Vous ne pouvez pas supprimer la pièce comptable d'un exercice clôturé !");
			
			if(item.status() == PieceStatus.ACCOUNTED)
				throw new IllegalArgumentException("Vous ne pouvez pas supprimer une pièce comptabilisée !");
			
			item.articles().deleteAll();
			ds.delete(item.id());
		}
	}

	@Override
	public Piece add(LocalDate date, String reference, String origin, String notes, Module moduleOrigin, LocalDate dateEcheance, Tiers tiers) throws IOException {		
		return add(UUID.randomUUID(), date, reference, origin, notes, moduleOrigin, dateEcheance, tiers);
	}

	@Override
	public Piece add(LocalDate date, String reference, String origin, String notes, LocalDate dateEcheance, Tiers tiers) throws IOException {
		return add(date, reference, origin, notes, module, dateEcheance, tiers);
	}

	@Override
	public Pieces of(PieceStatus status) throws IOException {
		return new PiecesDb(base, module, pieceType, status, period, reference, moduleOrigin);
	}

	@Override
	public Pieces of(PieceType pieceType) throws IOException {
		return new PiecesDb(base, module, pieceType, status, period, reference, moduleOrigin);
	}

	@Override
	public Pieces of(Period period) throws IOException {
		return new PiecesDb(base, module, pieceType, status, period, reference, moduleOrigin);
	}

	@Override
	public Pieces withReference(String reference) throws IOException {
		return new PiecesDb(base, module, pieceType, status, period, reference, moduleOrigin);
	}

	@Override
	public Piece add(UUID id, LocalDate date, String reference, String origin, String notes, Module moduleOrigin, LocalDate dateEcheance, Tiers tiers) throws IOException {
		
		if(StringUtils.isBlank(reference) && !pieceType.isNone()){
			int numberOfIteration = 0;
			boolean referenceExists = false;
			
			do {
				numberOfIteration++;
				reference = pieceType.sequence().withDateReference(date).generate();
				referenceExists = withReference(reference).all().size() > 0;
				
			} while (referenceExists && numberOfIteration < 30);	
			
			if(referenceExists)
				throw new IllegalArgumentException("La génération automatique de référence a échoué : veuillez, SVP, configurer la séquence associée à la pièce !");
		}
		
		PieceDb.validate(date, reference, pieceType, dateEcheance);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.pieceDateKey(), java.sql.Date.valueOf(date));	
		params.put(dm.referenceKey(), reference);
		params.put(dm.notesKey(), notes);
		params.put(dm.typeIdKey(), pieceType.id());
		params.put(dm.originModuleIdKey(), moduleOrigin.id());
		params.put(dm.statusKey(), PieceStatus.UNACCOUNTED.id());
		params.put(dm.originKey(), origin);
		params.put(dm.dateEcheanceKey(), dateEcheance == null ? null : java.sql.Date.valueOf(dateEcheance));
		
		if(tiers.isNone())
			params.put(dm.tiersIdKey(), pieceType.module().tiers().defaultTiers().id());
		else
			params.put(dm.tiersIdKey(), tiers.id());
		
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	public Pieces of(Module origin) throws IOException {
		return new PiecesDb(base, module, pieceType, status, period, reference, origin);
	}

	@Override
	protected Piece newOne(UUID id) {
		return new PieceDb(base, id, module);
	}

	@Override
	public Piece none() {
		return new PieceNone();
	}
}
