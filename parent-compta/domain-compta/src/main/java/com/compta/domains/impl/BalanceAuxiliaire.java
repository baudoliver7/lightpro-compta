package com.compta.domains.impl;

import com.compta.domains.api.BalanceBase;
import com.compta.domains.api.Compta;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.Period;
import com.infrastructure.datasource.Base;

public final class BalanceAuxiliaire extends BalanceBase {

	public BalanceAuxiliaire(Base base, Compta module, Period period, TiersType tiersType) {
		super(base, module, period, true, tiersType);
	}
}
