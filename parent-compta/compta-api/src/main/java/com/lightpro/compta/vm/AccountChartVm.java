package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.AccountChart;

public final class AccountChartVm {
	
	public final UUID id;
	public final String name;
	public final int codeDigits;	
	
	public AccountChartVm(){
		throw new UnsupportedOperationException("#AccountChartVm()");
	}
		
	public AccountChartVm(AccountChart origin){		
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.codeDigits = origin.codeDigits();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
