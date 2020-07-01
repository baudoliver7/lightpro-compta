package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.LineStatus;
import com.compta.domains.api.ReconcileStatus;
import com.compta.domains.api.TiersType;

public final class TiersTypeVm {

	public final UUID id;
	public final String name;
	public final String generalAccount;
	public final UUID generalAccountId;
	public final String sequence;
	public final UUID sequenceLettrageId;
	public final String sequenceLettrage;
	public final UUID sequenceId;
	public final int codeId;
	public final long completeReconcileCount;
	public final long partialReconcileCount;
	public final long unreconciledLineCount;
	
	public TiersTypeVm(){
		throw new UnsupportedOperationException("#TiersTypeVm()");
	}
		
	public TiersTypeVm(TiersType origin){
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.generalAccount = origin.generalAccount().name();
	        this.generalAccountId = origin.generalAccount().id();
	        this.sequence = origin.sequence().name();
	        this.sequenceLettrageId = origin.sequenceLettrage().id();
	        this.sequenceLettrage = origin.sequenceLettrage().name();
	        this.sequenceId = origin.sequence().id();
	        this.codeId = origin.code().id();
	        this.completeReconcileCount = origin.reconciles().of(ReconcileStatus.COMPLETE).count();
	        this.partialReconcileCount = origin.reconciles().of(ReconcileStatus.PARTIAL).count();
	        this.unreconciledLineCount = origin.lines().of(LineStatus.UNRECONCILED).count();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
}
