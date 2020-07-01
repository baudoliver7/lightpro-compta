package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.compta.domains.api.Reconcile;

public final class ReconcileVm {
	
	public final UUID id;
	public final String auxiliaryAccount;
	public final UUID auxiliaryAccountId;
	public final List<LineVm> lines;
	public final double debit;
	public final double credit;
	public final boolean isLettred;
	
	public ReconcileVm(){
		throw new UnsupportedOperationException("#ReconcileVm()");
	}
		
	public ReconcileVm(Reconcile origin){
		try {
			this.id = origin.id();
			this.auxiliaryAccount = origin.auxiliaryAccount().name();
			this.auxiliaryAccountId = origin.auxiliaryAccount().id();
	        this.lines = origin.allLines().stream().map(m -> new LineVm(m)).collect(Collectors.toList());
	        this.debit = origin.debit();
	        this.credit = origin.credit();
	        this.isLettred = origin.isLettred();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
}
