package com.compta.interfacage.sales.api;

import com.infrastructure.core.DomainMetadata;

public final class ModulePdvInterfaceMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public ModulePdvInterfaceMetadata() {
		this.domainName = "sales.module_pdvs";
		this.keyName = "id";
	}
	
	public ModulePdvInterfaceMetadata(final String domainName, final String keyName){
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
	
	public String nameKey(){
		return "name";
	}
	
	public String companyIdKey(){
		return "companyid";
	}
	
	public static ModulePdvInterfaceMetadata create(){
		return new ModulePdvInterfaceMetadata();
	}
}
