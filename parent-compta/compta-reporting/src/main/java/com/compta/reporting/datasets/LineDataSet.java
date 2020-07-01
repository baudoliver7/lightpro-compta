package com.compta.reporting.datasets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Journal;
import com.compta.domains.api.Line;
import com.compta.domains.api.PieceStatus;
import com.compta.domains.impl.JournalNone;
import com.infrastructure.core.Period;
import com.infrastructure.core.PojoDataSet;
import com.infrastructure.core.impl.PeriodBase;

public class LineDataSet extends PojoDataSet<Line> {

	@Override
	public void open(Object appContext, Map<String, Object> dataSetParamValues) throws IOException {
		try {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> mur = (Map<String, Object>)appContext;
			
			final Compta compta = (Compta)mur.get("moduleContext");
			
			LocalDate start = java.sql.Date.valueOf(dataSetParamValues.get("Start").toString()).toLocalDate();
			LocalDate end = java.sql.Date.valueOf(dataSetParamValues.get("End").toString()).toLocalDate();
			
			Journal journal = new JournalNone();
			if(!StringUtils.isBlank(dataSetParamValues.get("JournalId").toString()))
				journal = compta.journals().get(UUIDConvert.fromObject(dataSetParamValues.get("JournalId")));
				
			Period period = new PeriodBase(start, end);
			itr = compta.lines().of(period).of(journal).of(PieceStatus.ACCOUNTED).all().iterator();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}
}
