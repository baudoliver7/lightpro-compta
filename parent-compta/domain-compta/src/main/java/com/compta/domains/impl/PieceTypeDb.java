package com.compta.domains.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalPieceTypeMetadata;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.TiersType;
import com.compta.domains.api.Trame;
import com.compta.domains.api.Trames;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.PieceTypeMetadata;
import com.compta.domains.api.PieceTypeUsageCode;
import com.compta.domains.api.Pieces;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.securities.api.Sequence;

public final class PieceTypeDb extends GuidKeyEntityDb<PieceType, PieceTypeMetadata> implements PieceType {
	
	private final Compta module;
	
	public PieceTypeDb(final Base base, final UUID id, final Compta module){
		super(base, id, "Pièce introuvable !");
		this.module = module;
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public void update(String name, TiersType tiersTypeManaged, PieceNature nature, Sequence sequence, String object, Trame preferredTrame) throws IOException {
		
		validate(name, sequence);
		
		Map<String, Object> params = new HashMap<String, Object>();	
		params.put(dm.nameKey(), name);
		params.put(dm.tiersTypeManagedIdKey(), tiersTypeManaged.id());
		params.put(dm.natureIdKey(), nature.id());
		params.put(dm.objectKey(), object);
		params.put(dm.sequenceIdKey(), sequence.id());
		params.put(dm.preferredTrameIdKey(), preferredTrame.id());
		
		ds.set(params);	
	}

	@Override
	public PieceTypeUsageCode usageCode() throws IOException {
		int usageCodeId = ds.get(dm.usageCodeIdKey());
		return PieceTypeUsageCode.get(usageCodeId);
	}
	
	public static void validate(String name, Sequence sequence) throws IOException {
		
		if(StringUtils.isBlank(name))
			throw new IllegalArgumentException("Le libellé doit être renseigné !");
		
		if(sequence.isNone())
			throw new IllegalArgumentException("La séquence n'a pas été définie");
	}

	@Override
	public Trame preferredTrame() throws IOException {
		UUID trameId = ds.get(dm.preferredTrameIdKey());
		
		if(trameId == null)
			return new TrameNone();
		
		return new TrameDb(base, trameId, module);
	}

	@Override
	public TiersType tiersTypeManaged() throws IOException {
		UUID typeId = ds.get(dm.tiersTypeManagedIdKey());
		return module().tiersTypes().build(typeId);
	}

	@Override
	public Pieces pieces() throws IOException {
		return module().pieces().of(this);
	}
	
	@Override
	public Compta module() throws IOException {
		return module;
	}

	@Override
	public PieceNature nature() throws IOException {
		int natureId = ds.get(dm.natureIdKey());
		return PieceNature.get(natureId);
	}

	@Override
	public boolean echeanceManaged() throws IOException {
		PieceNature nature = nature();
		return nature == PieceNature.INVOICE;
	}

	@Override
	public String object() throws IOException {
		return ds.get(dm.objectKey());
	}

	@Override
	public Sequence sequence() throws IOException {
		UUID sequenceId = ds.get(dm.sequenceIdKey());
		return module().sequences().build(sequenceId);
	}

	@Override
	public Trames trames() throws IOException {
		return new TramesDb(base, this, module);
	}

	@Override
	public List<Journal> journals() throws IOException {
		
		JournalPieceTypeMetadata jrnlPtDm = JournalPieceTypeMetadata.create();
		String statement = String.format("SELECT %s FROM %s WHERE %s=?", jrnlPtDm.journalIdKey(), jrnlPtDm.domainName(), jrnlPtDm.pieceTypeIdKey());
		List<Object> params = Arrays.asList(id);
		
		DomainsStore ds = base.domainsStore(jrnlPtDm);
		return ds.find(statement, params)
				 .stream()
				 .map(m -> new JournalDb(base, UUIDConvert.fromObject(m), module))
				 .collect(Collectors.toList());
	}

	@Override
	public void changePreferredTrame(Trame trame) throws IOException {
		ds.set(dm.preferredTrameIdKey(), trame.id());
	}
}
