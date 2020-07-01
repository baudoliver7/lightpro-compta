package com.compta.interfacage.sales.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.impl.JournalNone;
import com.compta.domains.impl.PieceTypeNone;
import com.compta.domains.impl.TrameNone;
import com.compta.interfacage.sales.api.PaymentModeInterface;
import com.compta.interfacage.sales.api.PaymentModeInterfaceMetadata;
import com.compta.interfacage.sales.api.PaymentModeInterfaces;
import com.infrastructure.core.GuidKeyQueryableDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.securities.api.PaymentMode;

public final class PaymentModeInterfacesDb extends GuidKeyQueryableDb<PaymentModeInterface, PaymentModeInterfaceMetadata> implements PaymentModeInterfaces {

	private transient final Compta module;
	
	public PaymentModeInterfacesDb(Base base, Compta module){
		super(base, "Interface de mode de paiement introuvable !");
		this.module = module;
	}
	
	private PaymentModeInterface add(UUID id, Journal journalEncaissement, PieceType reglement, Trame acompteOrAvance, Trame reglementDefinitif, Trame provision) throws IOException{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.journalEncaissementIdKey(), journalEncaissement.id());
		params.put(dm.reglementIdKey(), reglement.id());	
		params.put(dm.acompteTrameIdKey(), acompteOrAvance.id());
		params.put(dm.reglementDefinitifTrameIdKey(), reglementDefinitif.id());
		params.put(dm.provisionTrameIdKey(), provision.id());
		
		DomainsStore ds = base.domainsStore(dm);
		
		ds.set(id, params);
		
		return newOne(id);
	}

	@Override
	public List<PaymentModeInterface> all() throws IOException {
		
		List<PaymentModeInterface> items = new ArrayList<PaymentModeInterface>();
		
		for (PaymentMode mode : module.paymentModes().all()) {
			if(ds.exists(mode.id()))
				items.add(newOne(mode.id()));
			else
				items.add(add(mode.id(), new JournalNone(), new PieceTypeNone(), new TrameNone(), new TrameNone(), new TrameNone()));				
		}
		
		return items;
	}

	@Override
	public PaymentModeInterface get(UUID id) throws IOException {
		PaymentModeInterface itemInter = null;
		
		for (PaymentModeInterface item : all()) {			
			if(item.id().equals(id))
				itemInter = item;
		}
		
		if(itemInter == null)
			throw new IllegalArgumentException(msgNotFound);
		
		return itemInter;
	}

	@Override
	protected PaymentModeInterface newOne(UUID id) {
		return new PaymentModeInterfaceDb(base, id, module);
	}

	@Override
	public PaymentModeInterface none() {
		return new PaymentModeInterfaceNone();
	}
}
