package com.compta.interfacage.sales.api;

import com.infrastructure.core.DomainMetadata;

public final class ProductTypeInterfaceMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public ProductTypeInterfaceMetadata() {
		this.domainName = "compta.sales_product_type_interfaces";
		this.keyName = "id";
	}
	
	public ProductTypeInterfaceMetadata(final String domainName, final String keyName){
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
	
	public String typeIdIdKey(){
		return "typeid";
	}
	
	public String accountIdKey(){
		return "accountid";
	}
	
	public static ProductTypeInterfaceMetadata create(){
		return new ProductTypeInterfaceMetadata();
	}	
}
