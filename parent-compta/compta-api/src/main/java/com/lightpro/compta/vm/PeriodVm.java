package com.lightpro.compta.vm;

import java.io.IOException;
import java.time.LocalDate;

import com.infrastructure.core.Period;

public final class PeriodVm {
	
	public final LocalDate start;
	public final LocalDate end;
	
	public PeriodVm(){
		throw new UnsupportedOperationException("#PeriodVm()");
	}
		
	public PeriodVm(Period origin){
		try {
			this.start = origin.start();
			this.end = origin.end();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
