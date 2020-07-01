package com.compta.reporting.datasets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.BalanceDetail;
import com.compta.domains.api.Compta;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.Period;
import com.infrastructure.core.PojoDataSet;
import com.infrastructure.core.impl.PeriodBase;

public final class BalanceAuxiliaireDataSet extends PojoDataSet<BalanceDetail> {

	@Override
	public void open(Object appContext, Map<String, Object> dataSetParamValues) throws IOException {
		try {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> mur = (Map<String, Object>)appContext;
			
			final Compta compta = (Compta)mur.get("moduleContext");
			
			UUID tiersTypeId = UUID.fromString(dataSetParamValues.get("TiersTypeId").toString());
			TiersType tiersType = compta.tiersTypes().get(tiersTypeId);
			LocalDate start = java.sql.Date.valueOf(dataSetParamValues.get("Start").toString()).toLocalDate();
			LocalDate end = java.sql.Date.valueOf(dataSetParamValues.get("End").toString()).toLocalDate();
			
			Period period = new PeriodBase(start, end);
			itr = compta.balanceAuxiliaire(tiersType, period).details().iterator();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}
}
