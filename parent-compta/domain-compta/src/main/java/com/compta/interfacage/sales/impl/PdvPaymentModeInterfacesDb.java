package com.compta.interfacage.sales.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.impl.JournalNone;
import com.compta.domains.impl.PieceTypeNone;
import com.compta.domains.impl.TrameNone;
import com.compta.interfacage.sales.api.ModulePdvInterface;
import com.compta.interfacage.sales.api.PdvPaymentModeInterface;
import com.compta.interfacage.sales.api.PdvPaymentModeInterfaceMetadata;
import com.compta.interfacage.sales.api.PdvPaymentModeInterfaces;
import com.infrastructure.core.GuidKeyQueryableDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.securities.api.PaymentMode;

public final class PdvPaymentModeInterfacesDb extends GuidKeyQueryableDb<PdvPaymentModeInterface, PdvPaymentModeInterfaceMetadata> implements PdvPaymentModeInterfaces {

	private transient final ModulePdvInterface pdv;
	private transient final Compta module;
	
	public PdvPaymentModeInterfacesDb(Base base, ModulePdvInterface pdv, Compta module){
		super(base, "Mode de paiement d'un point de vente introuvable !");
		this.pdv = pdv;
		this.module = module;	
		
		initialize();
	}
	
	private void initialize(){
		try {
			for (PaymentMode mode : module.paymentModes().all()) {
				if(!contains(mode)){
					add(mode, new JournalNone(), new PieceTypeNone(), new TrameNone(), new TrameNone(), new TrameNone());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		};
	}
	
	@Override
	public List<PdvPaymentModeInterface> all() throws IOException {
		String statement = String.format("SELECT %s FROM %s WHERE %s=?", dm.keyName(), dm.domainName(), dm.modulePdvIdKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(pdv.id());
		
		return ds.find(statement, params)
				 .stream()
				 .map(m -> build(UUIDConvert.fromObject(m)))
				 .collect(Collectors.toList());
	}
	
	@Override
	public PdvPaymentModeInterface get(UUID id) throws IOException {
		PdvPaymentModeInterface item = build(id);
		
		if(item.isNone())
			throw new IllegalArgumentException("Le mode de paiement n'a pas été trouvé !");
		
		return item;
	}

	@Override
	public boolean contains(PaymentMode mode) throws IOException {
		String statement = String.format("SELECT %s FROM %s WHERE %s=? AND %s=?", dm.keyName(), dm.domainName(), dm.paymentModeIdKey(), dm.modulePdvIdKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(mode.id());
		params.add(pdv.id());
		
		return ds.find(statement, params).size() > 0;
	}

	@Override
	public PdvPaymentModeInterface get(PaymentMode mode) throws IOException {
		
		if(!contains(mode))
			throw new IllegalArgumentException("Le mode de paiement n'a pas été trouvé !");
		
		String statement = String.format("SELECT %s FROM %s WHERE %s=? AND %s=?", dm.keyName(), dm.domainName(), dm.paymentModeIdKey(), dm.modulePdvIdKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(mode.id());
		params.add(pdv.id());
		
		List<Object> results = ds.find(statement, params);
		return build(UUIDConvert.fromObject(results.get(0)));
	}

	private PdvPaymentModeInterface add(PaymentMode mode, Journal journalEncaissement, PieceType reglement, Trame acompteOrAvance, Trame reglementDefinitif, Trame provision) throws IOException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.journalEncaissementIdKey(), journalEncaissement.id());
		params.put(dm.reglementIdKey(), reglement.id());	
		params.put(dm.acompteTrameIdKey(), acompteOrAvance.id());
		params.put(dm.reglementDefinitifTrameIdKey(), reglementDefinitif.id());
		params.put(dm.provisionTrameIdKey(), provision.id());
		params.put(dm.modulePdvIdKey(), pdv.id());
		params.put(dm.paymentModeIdKey(), mode.id());
		
		DomainsStore ds = base.domainsStore(dm);
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	protected PdvPaymentModeInterface newOne(UUID id) {
		return new PdvPaymentModeInterfaceDb(base, id, module);
	}

	@Override
	public PdvPaymentModeInterface none() {
		return new PdvPaymentModeInterfaceNone();
	}
}
