package com.lightpro.compta.vm;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.compta.domains.api.Line;

public final class LineVm {
	
	public final UUID id;
	public final LocalDate date;
	public final UUID generalAccountId;
	public final String generalAccount;
	public final UUID auxiliaryAccountId;
	public final String auxiliaryAccount;
	public final double debit;
	public final double credit;
	public final int sensId;
	public final String sens;
	public final String object;
	public final int pieceNatureId;
	public final String pieceNature;
	
	public LineVm(){
		throw new UnsupportedOperationException("#LineVm()");
	}
		
	public LineVm(Line origin){
		try {
			this.id = origin.id();
			this.date = origin.piece().date();
			this.generalAccountId = origin.generalAccount().id();
	        this.generalAccount = origin.generalAccount().fullName();
	        this.auxiliaryAccountId = origin.auxiliaryAccount().id();
	        this.auxiliaryAccount = origin.auxiliaryAccount().fullName();
	        this.debit = origin.debit();
	        this.credit = origin.credit();
	        this.sensId = origin.sens().id();
	        this.sens = origin.sens().toString();
	        this.object = origin.flux().object();
	        this.pieceNatureId = origin.piece().nature().id();
	        this.pieceNature = origin.piece().nature().toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
