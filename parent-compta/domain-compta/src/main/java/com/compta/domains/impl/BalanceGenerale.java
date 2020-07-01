package com.compta.domains.impl;

import com.compta.domains.api.BalanceBase;
import com.compta.domains.api.Compta;
import com.infrastructure.core.Period;
import com.infrastructure.datasource.Base;

public final class BalanceGenerale extends BalanceBase {

	public BalanceGenerale(Base base, Compta module, Period period) {
		super(base, module, period, false, new TiersTypeNone());
	}

}
