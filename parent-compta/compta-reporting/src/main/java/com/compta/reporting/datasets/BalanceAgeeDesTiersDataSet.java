package com.compta.reporting.datasets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Account;
import com.compta.domains.api.AccountNature;
import com.compta.domains.api.BalanceAgeeDesTiersDetail;
import com.compta.domains.api.Compta;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.PojoDataSet;

public final class BalanceAgeeDesTiersDataSet extends PojoDataSet<BalanceAgeeDesTiersDetail> {

	@Override
	public void open(Object appContext, Map<String, Object> dataSetParamValues) throws IOException {
		try {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> mur = (Map<String, Object>)appContext;
			
			final Compta compta = (Compta)mur.get("moduleContext");
			
			UUID tiersTypeId = UUID.fromString(dataSetParamValues.get("TiersTypeId").toString());
			TiersType tiersType = compta.tiersTypes().get(tiersTypeId);
			UUID auxiliaryAccountId = UUIDConvert.fromObject(dataSetParamValues.get("AuxiliaryAccountId"));
			Account auxiliaryAccount = compta.accounts().of(AccountNature.AUXILIARY).build(auxiliaryAccountId);
			LocalDate startDate = java.sql.Date.valueOf(dataSetParamValues.get("Start").toString()).toLocalDate();			
			
			itr = compta.balanceAgeeDesTiers(tiersType, auxiliaryAccount, startDate).details().iterator();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}
}

