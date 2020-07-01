package com.compta.reporting.datasets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import com.compta.domains.api.BalanceDetail;
import com.compta.domains.api.Compta;
import com.infrastructure.core.Period;
import com.infrastructure.core.PojoDataSet;
import com.infrastructure.core.impl.PeriodBase;

public final class BalanceGeneraleDataSet extends PojoDataSet<BalanceDetail> {

	@Override
	public void open(Object appContext, Map<String, Object> dataSetParamValues) throws IOException {
		try {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> mur = (Map<String, Object>)appContext;
			
			final Compta compta = (Compta)mur.get("moduleContext");
			
			LocalDate start = java.sql.Date.valueOf(dataSetParamValues.get("Start").toString()).toLocalDate();
			LocalDate end = java.sql.Date.valueOf(dataSetParamValues.get("End").toString()).toLocalDate();
			
			Period period = new PeriodBase(start, end);
			itr = compta.balanceGenerale(period).details().iterator();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}
}
