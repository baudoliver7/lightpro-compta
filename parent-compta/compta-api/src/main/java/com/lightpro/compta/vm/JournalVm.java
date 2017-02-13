package com.lightpro.compta.vm;

import java.io.IOException;
import java.util.UUID;

import com.compta.domains.api.Journal;
import com.fasterxml.jackson.annotation.JsonGetter;

public class JournalVm {
	
	private final transient Journal origin;
	
	public JournalVm() {
		throw new UnsupportedOperationException("#JournalVm()");
	}
		
	public JournalVm(Journal origin){
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
	public String code() throws IOException {
		return this.origin.code();
	}
	
	@JsonGetter
	public int typeId() throws IOException {
		return this.origin.type().id();
	}	
	
	@JsonGetter
	public String type() throws IOException {
		return this.origin.type().toString();
	}	
}
