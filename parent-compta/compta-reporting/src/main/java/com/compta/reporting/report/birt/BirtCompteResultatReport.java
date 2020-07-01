package com.compta.reporting.report.birt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.compta.domains.api.Compta;
import com.compta.domains.api.Exercise;

public final class BirtCompteResultatReport extends BirtComptaReport {

	private transient final Exercise exercise;
	
	public BirtCompteResultatReport(Exercise exercise, Compta module) {
		super("compte_resultat", module);
		
		this.exercise = exercise;
	}

	@Override
	public void render(String format, OutputStream output) throws IOException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		if(exercise.isNone())
			throw new IllegalArgumentException("Vous devez spécifier un exercice !");
		
		parameters.put("ExerciseId", exercise.id().toString());
		
		super.render(module, format, output, parameters);
	}
}
