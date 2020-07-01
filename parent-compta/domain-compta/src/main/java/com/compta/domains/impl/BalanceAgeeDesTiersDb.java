package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.compta.domains.api.Account;
import com.compta.domains.api.AccountNature;
import com.compta.domains.api.BalanceAgeeDesTiers;
import com.compta.domains.api.BalanceAgeeDesTiersDetail;
import com.compta.domains.api.Compta;
import com.compta.domains.api.TiersType;
import com.infrastructure.datasource.Base;

public final class BalanceAgeeDesTiersDb implements BalanceAgeeDesTiers {

	private transient final Base base;
	private transient final TiersType tiersType;
	private transient final LocalDate startDate;
	private transient final Account auxiliaryAccount;
	private transient final Compta module;
	private transient boolean isCached = false;
	private transient List<BalanceAgeeDesTiersDetail> details;
	
	public BalanceAgeeDesTiersDb(final Base base, final Compta module, final Account auxiliaryAccount, final TiersType tiersType, final LocalDate startDate){
		this.tiersType = tiersType;
		this.startDate = startDate;
		this.auxiliaryAccount = auxiliaryAccount;
		this.module = module;
		this.base = base;
	}
	
	@Override
	public TiersType tiersType() throws IOException {
		return tiersType;
	}

	@Override
	public LocalDate startDate() throws IOException {
		return startDate;
	}

	@Override
	public double montantNonEchu() throws IOException {
		double sum = 0;
		
		for (BalanceAgeeDesTiersDetail detail : details()) {
			sum += detail.montantNonEchu();
		}
		
		return sum;
	}

	@Override
	public double montantPeriod0A30() throws IOException {
		double sum = 0;
		
		for (BalanceAgeeDesTiersDetail detail : details()) {
			sum += detail.montantPeriod0A30();
		}
		
		return sum;
	}

	@Override
	public double montantPeriod30A60() throws IOException {
		double sum = 0;
		
		for (BalanceAgeeDesTiersDetail detail : details()) {
			sum += detail.montantPeriod30A60();
		}
		
		return sum;
	}

	@Override
	public double montantPeriod60A90() throws IOException {
		double sum = 0;
		
		for (BalanceAgeeDesTiersDetail detail : details()) {
			sum += detail.montantPeriod60A90();
		}
		
		return sum;
	}

	@Override
	public double montantPeriod90A120() throws IOException {
		double sum = 0;
		
		for (BalanceAgeeDesTiersDetail detail : details()) {
			sum += detail.montantPeriod90A120();
		}
		
		return sum;
	}

	@Override
	public double montantPeriodPlus120() throws IOException {
		double sum = 0;
		
		for (BalanceAgeeDesTiersDetail detail : details()) {
			sum += detail.montantPeriodPlus120();
		}
		
		return sum;
	}

	@Override
	public double montantTotal() throws IOException {
		double sum = 0;
		
		for (BalanceAgeeDesTiersDetail detail : details()) {
			sum += detail.montantTotal();
		}
		
		return sum;
	}

	@Override
	public List<BalanceAgeeDesTiersDetail> details() throws IOException {
		if(isCached)
			return details;
		
		this.isCached = true;			
		details = new ArrayList<BalanceAgeeDesTiersDetail>();
		
		if(!auxiliaryAccount.isNone()){
			BalanceAgeeDesTiersDetail detail = new BalanceAgeeDesTiersDetailDb(base, auxiliaryAccount, startDate);
			if(detail.montantTotal() != 0)
				details.add(detail);
		}else{
			List<Account> auxiliaryAccounts = module.accounts().of(AccountNature.AUXILIARY).of(tiersType).all();
			for (Account account : auxiliaryAccounts) {
				BalanceAgeeDesTiersDetail detail = new BalanceAgeeDesTiersDetailDb(base, account, startDate);
				if(detail.montantTotal() != 0)
					details.add(detail);
			}
		}
		
		return details;
	}

}
