package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Account;

public final class AccountVm {
	
	public final UUID id;
	public final String name;
	public final String fullName;
	public final String code;
	public final int typeId;
	public final String type;
	public final boolean refuseCreditBalance;
	public final boolean refuseDebitBalance;
	
	public AccountVm(){
		throw new UnsupportedOperationException("#AccountVm()");
	}
		
	public AccountVm(Account origin){
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.fullName = origin.fullName();
	        this.code = origin.code();
	        this.typeId = origin.type().id();
	        this.type = origin.type().toString();
	        this.refuseCreditBalance = origin.refuseCreditBalance();
	        this.refuseDebitBalance = origin.refuseDebitBalance();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
