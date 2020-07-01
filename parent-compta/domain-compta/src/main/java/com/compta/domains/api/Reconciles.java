package com.compta.domains.api;

import java.io.IOException;

import com.infrastructure.core.GuidKeyAdvancedQueryable;

public interface Reconciles extends GuidKeyAdvancedQueryable<Reconcile> {	
	Reconcile add() throws IOException;
	Reconciles of(Account auxiliaryAccount) throws IOException;
	Reconciles of(TiersType tiersType) throws IOException;
	Reconciles of(ReconcileStatus status) throws IOException;
}
