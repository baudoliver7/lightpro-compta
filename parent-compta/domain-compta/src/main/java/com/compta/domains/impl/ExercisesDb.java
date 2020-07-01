package com.compta.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.compta.domains.api.Compta;
import com.compta.domains.api.Exercise;
import com.compta.domains.api.Exercise.ExerciseStatus;
import com.compta.domains.api.ExerciseMetadata;
import com.compta.domains.api.Exercises;
import com.compta.domains.api.PieceStatus;
import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.core.Period;
import com.infrastructure.core.impl.PeriodBase;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;

public final class ExercisesDb extends GuidKeyAdvancedQueryableDb<Exercise, ExerciseMetadata> implements Exercises {

	private transient final Compta module;
	private transient final ExerciseStatus status;
	private transient final LocalDate date;
	
	public ExercisesDb(Base base, Compta module, ExerciseStatus status, LocalDate date) {
		super(base, "Exercice introuvable !");
		
		this.module = module;
		this.status = status;
		this.date = date;
	}

	@Override
	public boolean contains(Exercise item) {
		try {
			return !item.isNone() && item.module().equals(module);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public Exercise open(Period period) throws IOException {
		
		if(module.journalRan().isNone())
			throw new IllegalArgumentException("Vous devez configurer le journal des reports à nouveau avant de continuer cette action !");
		
		Period exoPeriod = new ExercisePeriod(period);
		exoPeriod.validate();
		
		Exercise exo1 = get(period.start());
		Exercise exo2 = get(period.end());
		
		if(!exo1.isNone() || !exo2.isNone())
			throw new IllegalArgumentException("La période de l'exercice chevauche celle d'un autre exercice !"); 
				
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.openedDateKey(), java.sql.Date.valueOf(LocalDate.now()));
		params.put(dm.startKey(), java.sql.Date.valueOf(period.start()));
		params.put(dm.endKey(), java.sql.Date.valueOf(period.end()));
		params.put(dm.moduleIdKey(), module.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	public Exercise get(LocalDate date) throws IOException {
		if(date == null)
			throw new IllegalArgumentException("Date invalide !");
		
		List<Exercise> items = of(ExerciseStatus.NONE, date).find(1, 1, "");
		return items.isEmpty() ? none() : items.get(0);
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		
		String statement = String.format("%s exo "
				+ "WHERE exo.%s=?",				 
				dm.domainName(), 
				dm.moduleIdKey());
		
		params.add(module.id());
		
		if(status == ExerciseStatus.CLOSED){
			statement = String.format("%s AND exo.%s IS NOT NULL", statement, dm.closedDateKey());
		}else {
			if(status == ExerciseStatus.UNCLOSED)
				statement = String.format("%s AND exo.%s IS NULL", statement, dm.closedDateKey());
		}
		
		if(date != null){
			statement = String.format("%s AND ? BETWEEN exo.%s AND exo.%s ", statement, dm.startKey(), dm.endKey());
			params.add(java.sql.Date.valueOf(date));
		}
	
		String orderClause = String.format("ORDER BY exo.%s DESC", dm.startKey());
				
		String keyResult = String.format("exo.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected UUID convertKey(Object id) {
		return UUIDConvert.fromObject(id);
	}

	@Override
	public Exercise none() {
		return new ExerciseNone();
	}

	@Override
	protected Exercise newOne(UUID id) {
		return new ExerciseDb(base, id, module);
	}

	@Override
	public Exercise openNext() throws IOException {
		
		Exercise lastExo = of(ExerciseStatus.NONE, null).last();
		LocalDate start = LocalDate.now();
		
		if(!lastExo.isNone())
			start = lastExo.period().end().plusDays(1);
		
		LocalDate end = start.withMonth(module.lastMonthExo()).withDayOfMonth(module.lastDayExo());
		return open(new PeriodBase(start, end));
	}

	@Override
	public Exercise current() throws IOException {
		return get(LocalDate.now());
	}

	@Override
	public Exercise last() throws IOException {
		List<Exercise> exos = of(ExerciseStatus.NONE, null).find(1, 1, "");
		return exos.isEmpty() ? none() : exos.get(0);
	}

	@Override
	public Exercises of(ExerciseStatus status, LocalDate date) throws IOException {
		return new ExercisesDb(base, module, status, date);
	}	
	
	@Override
	public void delete(Exercise item) throws IOException {
		
		if(item.isClosed())
			throw new IllegalArgumentException("Vous ne pouvez pas supprimer un exercice clôturé !");
		
		boolean hasAnyLineAccounted = module.lines().of(item.period()).of(PieceStatus.ACCOUNTED).count() > 0;
		if(hasAnyLineAccounted)
			throw new IllegalArgumentException("Impossible de supprimer l'exercice : il contient des écritures comptabilisées !");
		
		super.delete(item);
	}
}
