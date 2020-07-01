package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Compta;
import com.compta.domains.api.CompteResultat;
import com.compta.domains.api.Exercise;
import com.compta.domains.api.ExerciseMetadata;
import com.compta.domains.api.PieceStatus;
import com.compta.domains.api.Pieces;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.core.Period;
import com.infrastructure.core.impl.PeriodBase;
import com.infrastructure.datasource.Base;

public final class ExerciseDb extends GuidKeyEntityDb<Exercise, ExerciseMetadata> implements Exercise {

	private final Compta module;
	
	public ExerciseDb(Base base, UUID id, Compta module) {
		super(base, id, "Exercice introuvable !");
		this.module = module;
	}

	@Override
	public Period period() throws IOException {
		java.sql.Date start = ds.get(dm.startKey());
		java.sql.Date end = ds.get(dm.endKey());
		
	    return new ExercisePeriod(new PeriodBase(start.toLocalDate(), end.toLocalDate())); 
	}

	@Override
	public boolean isClosed() throws IOException {
		return closedDate() != null;
	}

	@Override
	public LocalDate closedDate() throws IOException {
		java.sql.Date date = ds.get(dm.closedDateKey());
		return date == null ? null : date.toLocalDate();
	}

	@Override
	public Compta module() throws IOException {
		return module;
	}

	@Override
	public void update(Period period) throws IOException {
		
		Period exoPeriod = new ExercisePeriod(period);		
		exoPeriod.validate();
		
		Exercise exo1 = module().exercises().get(period.start());
		Exercise exo2 = module().exercises().get(period.end());
		
		if(!exo1.isNone() || !exo2.isNone())
			throw new IllegalArgumentException("La période de l'exercice chevauche celle d'un autre exercice !");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.startKey(), java.sql.Date.valueOf(period.start()));
		params.put(dm.endKey(), java.sql.Date.valueOf(period.end()));
		
		ds.set(params);		
	}

	@Override
	public void close() throws IOException {
		
		if(isClosed())
			throw new IllegalArgumentException("L'exercice est déjà clôturé !");
		
		List<Exercise> opened = module().exercises().of(ExerciseStatus.UNCLOSED, null).all();
		Exercise exoBeforeUnclosed = new ExerciseNone();
		
		for (Exercise exo : opened) {
			if(!exo.period().start().isEqual(period().start()) && !exo.isClosed())
				exoBeforeUnclosed = exo;
				
			if(exo.period().start() == period().start()){
				break;
			}
		}
		
		if(!exoBeforeUnclosed.isNone())
			throw new IllegalArgumentException("Tous les exercices antérieurs doivent être d'abord clôturés !");
		
		if(module().journalRan().isNone())
			throw new IllegalArgumentException("Vous devez configurer le journal des reports à nouveau avant de continuer cette action !");
		
		if(pieces().of(PieceStatus.ACCOUNTED).count() > 0)
			throw new IllegalArgumentException("Vous devez comptabilitser toutes les écritures passées de l'exercice avant de continuer cette action !");
		
		ds.set(dm.closedDateKey(), java.sql.Date.valueOf(LocalDate.now()));
	}

	@Override
	public LocalDate openedDate() throws IOException {
		java.sql.Date date = ds.get(dm.openedDateKey());
		return date.toLocalDate();
	}

	@Override
	public Pieces pieces() throws IOException {
		return module().pieces().of(period());
	}

	@Override
	public CompteResultat compteResultat() throws IOException {
		return new CompteResultatImpl(this);
	}

	@Override
	public String name() throws IOException {		
		return String.format("%s - %s", period().start().toString(), period().end().toString());
	}
}
