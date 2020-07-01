package com.compta.domains.api;

import java.io.IOException;

import com.infrastructure.core.GuidKeyQueryable;
import com.securities.api.Sequence;

public interface TiersTypes extends GuidKeyQueryable<TiersType> {	
	TiersType add(String name, Account generalAccount, Sequence sequence, Sequence sequenceLettrage) throws IOException;
	TiersType add(String name, TiersCode code, Account generalAccount, Sequence sequence, Sequence sequenceLettrage) throws IOException;
	TiersType none();
}
