package com.compta.domains.api;

import com.infrastructure.core.DomainMetadata;

public class ExerciseMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public ExerciseMetadata() {
		this.domainName = "compta.exercises";
		this.keyName = "id";
	}
	
	public ExerciseMetadata(final String domainName, final String keyName){
		this.domainName = domainName;
		this.keyName = keyName;
	}
	
	@Override
	public String domainName() {
		return this.domainName;
	}

	@Override
	public String keyName() {
		return this.keyName;
	}
	
	public String startKey(){
		return "start";
	}
	
	public String endKey(){
		return "end_l";
	}
	
	public String openedDateKey(){
		return "openeddate";
	}
	
	public String closedDateKey(){
		return "closeddate";
	}
	
	public String moduleIdKey(){
		return "moduleid";
	}

	public static ExerciseMetadata create(){
		return new ExerciseMetadata();
	}	
}
