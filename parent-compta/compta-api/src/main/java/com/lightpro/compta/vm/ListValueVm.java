package com.lightpro.compta.vm;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonGetter;

public class ListValueVm {
	
	private final transient int id;
	private final transient String name;
	
	public ListValueVm(){
		throw new UnsupportedOperationException("#ListValueVm()");
	}
		
	public ListValueVm(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	@JsonGetter
	public int id(){
		return id;
	}
	
	@JsonGetter
	public String name() throws IOException {
		return name;
	}	
}
