package com.compta.domains.api;

import java.io.IOException;
import java.util.UUID;

public interface Tierss {
	Tiers build(UUID id);
	Tiers defaultTiers() throws IOException;
}
