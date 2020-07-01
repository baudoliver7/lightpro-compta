package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Journal;

public final class JournalVm {
	
	public final UUID id;
	public final String name;
	public final String code;
	public final int typeId;
	public final String type;
	public final UUID accountId;
	public final String account;
	public final String accountFullName;
	public final boolean isViewedOnDashboard;
	public final boolean validateAccount;
	
	public JournalVm() {
		throw new UnsupportedOperationException("#JournalVm()");
	}
		
	public JournalVm(Journal origin){
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.code = origin.code();	        
	        this.typeId = origin.type().id();
	        this.type = origin.type().toString();
	        this.accountId = origin.account().id();
	        this.account = origin.account().name();
	        this.accountFullName = origin.account().fullName();
	        this.isViewedOnDashboard = origin.isViewedOnDashboard();
	        this.validateAccount = origin.validateAccount();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
