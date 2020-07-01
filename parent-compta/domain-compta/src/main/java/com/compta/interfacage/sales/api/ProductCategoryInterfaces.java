package com.compta.interfacage.sales.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProductCategoryInterfaces {
	List<ProductCategoryInterface> all() throws IOException;
	ProductCategoryInterface get(UUID id) throws IOException;
}