package com.compta.domains.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalMetadata;
import com.compta.domains.api.JournalPieceTypeMetadata;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.PieceType;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;

public final class JournalDb extends GuidKeyEntityDb<Journal, JournalMetadata> implements Journal {
	
	private final Compta module;
	
	public JournalDb(final Base base, final UUID id, final Compta module){
		super(base, id, "Journal introuvable !");
		this.module = module;
	}

	@Override
	public String code() throws IOException {
		return ds.get(dm.codeKey());
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public JournalType type() throws IOException {
		int typeId = ds.get(dm.typeIdKey());
		return JournalType.get(typeId);				
	}

	@Override
	public Compta module() throws IOException {
		return module;
	}

	@Override
	public void update(String code, String name, JournalType type, Account account, boolean validateAccount) throws IOException {
		
		validate(code, name, type, account);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.codeKey(), code);	
		params.put(dm.nameKey(), name);
		params.put(dm.typeIdKey(), type.id());
		params.put(dm.accountIdKey(), account.id());
		
		if(account.id() == null)
			params.put(dm.validateAccountKey(), false);
		else
			params.put(dm.validateAccountKey(), validateAccount);
		
		ds.set(params);	
	}	
	
	public static void validate(String code, String name, JournalType type, Account account) throws IOException {
		
		if (StringUtils.isBlank(code))
            throw new IllegalArgumentException("Invalid code : it can't be empty!");
		
		if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
				
		if((type == JournalType.BANQUE || type == JournalType.LIQUIDITES) && account.id() == null)
			throw new IllegalArgumentException("Vous devez spécifier un compte pour un journal de banque ou de caisse !");
	}

	@Override
	public Account account() throws IOException {
		UUID accountId = ds.get(dm.accountIdKey());
		return module().accounts().build(accountId);
	}

	@Override
	public boolean isViewedOnDashboard() {
		try {
			return ds.get(dm.viewOnDashboardKey());
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void viewOnDashboard(boolean active) throws IOException {
		ds.set(dm.viewOnDashboardKey(), active);
	}

	@Override
	public List<PieceType> pieceTypes() throws IOException {
		return module().pieceTypes(this).all();
	}

	@Override
	public void addPieceType(PieceType type) throws IOException {
		
		if(type.id() == null)
			throw new IllegalArgumentException("Le type de pièce à ajouter n'existe pas !");
		
		if(module().pieceTypes(this).contains(type))
			throw new IllegalArgumentException("Ce type de pièce a déjà été ajouté !");
		
		if((type() == JournalType.ACHATS || type() == JournalType.VENTE) && type.nature() != PieceNature.INVOICE)
			throw new IllegalArgumentException("Type de pièce invalide : vous devez ajouter uniquement qu'une facture pour un journal de vente ou d'achats !");
		
		JournalPieceTypeMetadata dm = JournalPieceTypeMetadata.create();
		DomainsStore ds = base.domainsStore(dm);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.journalIdKey(), id);
		params.put(dm.pieceTypeIdKey(), type.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
	}

	@Override
	public void removePieceType(PieceType type) throws IOException {
		
		if(module().pieceTypes(this).contains(type)){
			JournalPieceTypeMetadata dm = JournalPieceTypeMetadata.create();
			String statement = String.format("DELETE FROM %s WHERE %s=? AND %s=?", dm.domainName(), dm.journalIdKey(), dm.pieceTypeIdKey());
			
			base.executeUpdate(statement, Arrays.asList(id, type.id()));
		}		
	}

	@Override
	public boolean validateAccount() {
		try {
			return ds.get(dm.validateAccountKey());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
