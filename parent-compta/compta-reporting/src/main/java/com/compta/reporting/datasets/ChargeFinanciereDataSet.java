package com.compta.reporting.datasets;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.domains.api.CompteResultatDetail;
import com.compta.domains.api.Exercise;
import com.infrastructure.core.PojoDataSet;

public final class ChargeFinanciereDataSet extends PojoDataSet<CompteResultatDetail> {

	@Override
	public void open(Object appContext, Map<String, Object> dataSetParamValues) throws IOException {
		try {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> mur = (Map<String, Object>)appContext;
			
			final Compta compta = (Compta)mur.get("moduleContext");
			
			UUID exerciseId = UUID.fromString(dataSetParamValues.get("ExerciseId").toString());
			Exercise exercise = compta.exercises().get(exerciseId);			
			
			itr = exercise.compteResultat().chargesFinancieres().iterator();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}
}

