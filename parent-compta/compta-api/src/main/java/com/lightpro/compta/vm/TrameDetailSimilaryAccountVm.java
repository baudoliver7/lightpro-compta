package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Account;

public final class TrameDetailSimilaryAccountVm {
	
	public final UUID id;
	public final String name;
	
	
	public TrameDetailSimilaryAccountVm(){
		throw new UnsupportedOperationException("#TrameDetailSimilaryAccountVm()");
	}
		
	public TrameDetailSimilaryAccountVm(Account origin){
		try {
			this.id = origin.id();
			this.name = origin.fullName();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
}
