package com.compta.domains.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BalanceAgeeDesTiers {	
	TiersType tiersType() throws IOException;
	LocalDate startDate() throws IOException;
	
	public double montantNonEchu() throws IOException;
	public double montantPeriod0A30() throws IOException;
	public double montantPeriod30A60() throws IOException;
	public double montantPeriod60A90() throws IOException;
	public double montantPeriod90A120() throws IOException;
	public double montantPeriodPlus120() throws IOException;
	public double montantTotal() throws IOException;
	
	public List<BalanceAgeeDesTiersDetail> details() throws IOException;
}
