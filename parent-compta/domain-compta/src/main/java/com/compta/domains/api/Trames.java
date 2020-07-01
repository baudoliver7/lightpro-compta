package com.compta.domains.api;

import java.io.IOException;

import com.infrastructure.core.GuidKeyAdvancedQueryable;

public interface Trames extends GuidKeyAdvancedQueryable<Trame> {

	Trame add(String name) throws IOException;
	void deleteAll() throws IOException;	
}
