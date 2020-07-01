package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.compta.domains.api.TrameFluxDetail;

public final class TrameDetailVm {
	
	public final UUID id;
	public final UUID generalAccountId;
	public final String generalAccount;
	public final int sensId;
	public final String sens;
	public final boolean isAggregateAccount;
	public final String formular;
	public final List<TrameDetailSimilaryAccountVm> similaryAccounts;
	
	public TrameDetailVm(){
		throw new UnsupportedOperationException("#TrameDetailVm()");
	}
		
	public TrameDetailVm(TrameFluxDetail origin){
		try {
			this.id = origin.id();
			this.generalAccountId = origin.generalAccount().id();
			this.generalAccount = origin.generalAccount().fullName();
	        this.sensId = origin.sens().id();
	        this.sens = origin.sens().toString();
	        this.isAggregateAccount = origin.isAggregateAccount();
	        this.formular = origin.formular().expression();
	        this.similaryAccounts = origin.similaryAccounts().all().stream().map(m -> new TrameDetailSimilaryAccountVm(m)).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
}
