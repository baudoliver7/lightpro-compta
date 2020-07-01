package com.compta.interfacage.sales.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.impl.TrameNone;
import com.compta.interfacage.sales.api.PaymentModeInterface;
import com.compta.interfacage.sales.api.PaymentModeInterfaceMetadata;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.securities.api.PaymentMode;

public final class PaymentModeInterfaceDb extends GuidKeyEntityDb<PaymentModeInterface, PaymentModeInterfaceMetadata> implements PaymentModeInterface {

	private transient final Compta module;
	
	public PaymentModeInterfaceDb(Base base, UUID id, Compta module) {
		super(base, id, "Interface de mode de paiement introuvable !");	
		this.module = module;
	}

	@Override
	public Journal journalEncaissement() throws IOException {
		UUID journalId = ds.get(dm.journalEncaissementIdKey());
		return module.journals().build(journalId);
	}

	@Override
	public PieceType reglement() throws IOException {
		UUID reglementId = ds.get(dm.reglementIdKey());
		return module.pieceTypes().build(reglementId);
	}

	@Override
	public Trame acompteOrAvance() throws IOException {
		PieceType reglement = reglement();
		if(reglement.isNone())
			return new TrameNone();
			
		UUID trameId = ds.get(dm.acompteTrameIdKey());
		return reglement.trames().build(trameId);
	}

	@Override
	public Trame reglementDefinitif() throws IOException {
		PieceType reglement = reglement();
		if(reglement.isNone())
			return new TrameNone();
			
		UUID trameId = ds.get(dm.reglementDefinitifTrameIdKey());
		return reglement.trames().build(trameId);
	}

	@Override
	public PaymentMode paymentMode() throws IOException {
		return module.paymentModes().build(id);
	}

	@Override
	public void update(Journal journalEncaissement, PieceType reglement, Trame acompteOrAvance, Trame reglementDefinitif, Trame provision) throws IOException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.journalEncaissementIdKey(), journalEncaissement.id());
		params.put(dm.reglementIdKey(), reglement.id());	
		
		if(!reglement.isNone()){
			params.put(dm.acompteTrameIdKey(), acompteOrAvance.id());
			params.put(dm.reglementDefinitifTrameIdKey(), reglementDefinitif.id());
			params.put(dm.provisionTrameIdKey(), provision.id());
		}else{
			params.put(dm.acompteTrameIdKey(), null);
			params.put(dm.reglementDefinitifTrameIdKey(), null);
			params.put(dm.provisionTrameIdKey(), null);
		}		
		
		ds.set(params);		
	}

	@Override
	public Trame provision() throws IOException {
		PieceType reglement = reglement();
		if(reglement.isNone())
			return new TrameNone();
			
		UUID trameId = ds.get(dm.provisionTrameIdKey());
		return reglement.trames().build(trameId);
	}

}
