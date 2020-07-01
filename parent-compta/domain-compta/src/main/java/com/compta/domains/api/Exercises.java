package com.compta.domains.api;

import java.io.IOException;
import java.time.LocalDate;

import com.compta.domains.api.Exercise.ExerciseStatus;
import com.infrastructure.core.GuidKeyAdvancedQueryable;
import com.infrastructure.core.Period;

public interface Exercises extends GuidKeyAdvancedQueryable<Exercise> {
	Exercise open(Period period) throws IOException;
	Exercise openNext() throws IOException;
	Exercise get(LocalDate date) throws IOException;
	Exercise current() throws IOException;
	Exercise last() throws IOException;
	Exercises of(ExerciseStatus status, LocalDate date) throws IOException;
}
