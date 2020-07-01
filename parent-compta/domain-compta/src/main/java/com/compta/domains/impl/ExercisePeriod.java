package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;

import com.infrastructure.core.Period;

public class ExercisePeriod implements Period {

	private transient final Period origin;
	
	public ExercisePeriod(Period origin){
		this.origin = origin;
	}
	
	@Override
	public boolean contains(LocalDate date) {
		return origin.contains(date);
	}

	@Override
	public LocalDate end() throws IOException {
		return origin.end();
	}

	@Override
	public void validate() throws IOException {
		origin.validate();
	}

	@Override
	public LocalDate start() throws IOException {	
		return origin.start();
	}

	@Override
	public boolean isDefined() {
		return origin.isDefined();
	}

	@Override
	public boolean exclude(Period period) {
		return origin.exclude(period);
	}

	@Override
	public boolean include(Period period) {
		return origin.include(period);
	}

	@Override
	public boolean superposeWith(Period period) {
		return origin.superposeWith(period);
	}
}
