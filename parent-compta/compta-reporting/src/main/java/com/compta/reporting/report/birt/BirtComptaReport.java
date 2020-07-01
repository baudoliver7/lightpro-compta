package com.compta.reporting.report.birt;

import com.compta.domains.api.Compta;
import com.infrastructure.core.impl.BirtReport;

public abstract class BirtComptaReport extends BirtReport {

	protected transient final Compta module;
	
	public BirtComptaReport(String reportName, Compta module){
		super(reportName);
		
		this.module = module;
	}
	
	@Override
	public String directory() {
		return "";
	}
}
