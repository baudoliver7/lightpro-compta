package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.AccountChart;
import com.fasterxml.jackson.annotation.JsonGetter;

public class AccountChartVm {
	private final transient AccountChart origin;
	
	public AccountChartVm(){
		throw new UnsupportedOperationException("#AccountChartVm()");
	}
		
	public AccountChartVm(AccountChart origin){
		this.origin = origin;
	}
	
	@JsonGetter
	public UUID id(){
		return this.origin.id();
	}
	
	@JsonGetter
	public String name() throws IOException {
		return this.origin.name();
	}
	
	@JsonGetter
	public int codeDigits() throws IOException {
		return this.origin.codeDigits();
	}	
}
