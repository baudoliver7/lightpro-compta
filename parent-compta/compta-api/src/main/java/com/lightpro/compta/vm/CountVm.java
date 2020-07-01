package com.lightpro.compta.vm;

import com.fasterxml.jackson.annotation.JsonGetter;

public final class CountVm {
	private transient final long count;
	
	public CountVm(){
		throw new UnsupportedOperationException("#CountVm()");
	}
		
	public CountVm(long count){
		this.count = count;
	}
	
	@JsonGetter
	public long count(){
		return this.count;
	}
}
