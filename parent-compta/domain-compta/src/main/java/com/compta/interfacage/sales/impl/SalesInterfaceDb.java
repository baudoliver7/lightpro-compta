package com.compta.interfacage.sales.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.impl.JournalNone;
import com.compta.domains.impl.PieceTypeNone;
import com.compta.domains.impl.TrameNone;
import com.compta.interfacage.sales.api.InvoiceInterface;
import com.compta.interfacage.sales.api.InvoiceInterfaceMetadata;
import com.compta.interfacage.sales.api.PaymentInterface;
import com.compta.interfacage.sales.api.PdvInterface;
import com.compta.interfacage.sales.api.SalesInterface;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.sales.domains.api.Sales;
import com.sales.domains.impl.SalesDb;
import com.securities.api.Module;
import com.securities.api.ModuleType;

public final class SalesInterfaceDb implements SalesInterface {

	private final Compta compta;
	private final Base base;
	private Sales sales;
	
	public SalesInterfaceDb(final Base base, final Compta compta){
		this.base = base;
		this.compta = compta;
		
		initialize();
	}
	
	private void initialize()  {
		Module module;
		
		try {
			if(!compta.company().modulesInstalled().contains(ModuleType.SALES))
				module = compta.company().modulesProposed().get(ModuleType.SALES);
			else
				module = compta.company().modulesInstalled().get(ModuleType.SALES);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		sales = new SalesDb(base, module);
	}
	
	@Override
	public InvoiceInterface invoiceInterface() throws IOException {
		InvoiceInterfaceMetadata dm = new InvoiceInterfaceMetadata();
		DomainsStore ds = base.domainsStore(dm);
		
		if(ds.exists(compta.id()))
			return new InvoiceInterfaceDb(base, compta.id(), compta);
		else
			return add(new JournalNone(), new PieceTypeNone(), new TrameNone());
	}

	@Override
	public PaymentInterface paymentInterface() throws IOException {
		return new PaymentInterfaceDb(base, compta);
	}

	@Override
	public PdvInterface pdvInterface() throws IOException {
		return new PdvInterfaceDb(base, compta);
	}

	private InvoiceInterface add(Journal journalVente, PieceType factureClient, Trame factureDoitTrame) throws IOException {
		
		InvoiceInterfaceMetadata dm = new InvoiceInterfaceMetadata();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.journalVenteIdKey(), journalVente.id());	
		params.put(dm.factureClientIdKey(), factureClient.id());
		params.put(dm.factureDoitTrameIdKey(), factureDoitTrame.id());
		
		DomainsStore ds = base.domainsStore(dm);
		ds.set(compta.id(), params);
		
		return new InvoiceInterfaceDb(base, compta.id(), compta);
	}

	@Override
	public boolean available() {
		return sales.isInstalled();
	}
}
