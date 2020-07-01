package com.compta.interfacage.sales.api;

import java.io.IOException;
import java.util.List;

public interface ProductTypeInterfaces {
	List<ProductTypeInterface> all() throws IOException;
	ProductTypeInterface get(int id) throws IOException;
}
