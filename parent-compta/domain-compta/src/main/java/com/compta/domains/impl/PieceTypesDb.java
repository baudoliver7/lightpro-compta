package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalPieceTypeMetadata;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.TiersType;
import org.apache.commons.lang3.StringUtils;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.PieceTypeMetadata;
import com.compta.domains.api.PieceTypeUsageCode;
import com.compta.domains.api.PieceTypes;
import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.securities.api.Sequence;

public final class PieceTypesDb extends GuidKeyAdvancedQueryableDb<PieceType, PieceTypeMetadata> implements PieceTypes {

	private transient Compta module;
	private transient Journal journal;
	
	public PieceTypesDb(final Base base, final Compta module, Journal journal){
		super(base, "Type de pièce introuvable !");
		this.module = module;
		this.journal = journal;
	}
	
	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
	
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		String statement;
		
		JournalPieceTypeMetadata jnltpDm = JournalPieceTypeMetadata.create();
		statement = String.format("%s pt "
				+ "{clause-join-jrnlpt} "
				+ "WHERE pt.%s ILIKE ? AND pt.%s=?",				 
				dm.domainName(), 
				dm.nameKey(), dm.moduleIdKey());

		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(journal.isNone()){
			statement = statement.replace("{clause-join-jrnlpt}", StringUtils.EMPTY);
		}else{
			String clause = String.format("RIGHT JOIN %s jnltp on jnltp.%s=pt.%s", jnltpDm.domainName(), jnltpDm.pieceTypeIdKey(), dm.keyName());
			statement = statement.replace("{clause-join-jrnlpt}", clause);
			statement = String.format("%s AND jnltp.%s=?", statement, jnltpDm.journalIdKey());
			params.add(journal.id());			
		}
		
		String orderClause = String.format("ORDER BY pt.%s ASC", dm.nameKey());
		
		String keyResult = String.format("pt.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	public void delete(PieceType item) throws IOException {
		if(contains(item))
		{
			item.trames().deleteAll();
			ds.delete(item.id());
		}
	}

	@Override
	public PieceType add(String name, PieceTypeUsageCode usageCode, TiersType tiersTypeManaged, PieceNature nature, Sequence sequence, String object) throws IOException {
		
		PieceTypeDb.validate(name, sequence);
		
		Map<String, Object> params = new HashMap<String, Object>();	
		params.put(dm.nameKey(), name);
		params.put(dm.moduleIdKey(), module.id());
		params.put(dm.usageCodeIdKey(), usageCode.id());
		params.put(dm.tiersTypeManagedIdKey(), tiersTypeManaged.id());
		params.put(dm.natureIdKey(), nature.id());
		params.put(dm.objectKey(), object);
		params.put(dm.sequenceIdKey(), sequence.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	public PieceType add(String name, TiersType tiersTypeManaged, PieceNature nature, Sequence sequence, String object) throws IOException {
		return add(name, PieceTypeUsageCode.USER_USAGE, tiersTypeManaged, nature, sequence, object);
	}

	@Override
	public PieceType none() {
		return new PieceTypeNone();
	}

	@Override
	protected PieceType newOne(UUID id) {
		return new PieceTypeDb(base, id, module);
	}
}
