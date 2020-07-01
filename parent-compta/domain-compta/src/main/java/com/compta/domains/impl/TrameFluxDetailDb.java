/**
 * 
 */
package com.compta.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import com.common.utilities.formular.Formular;
import com.compta.domains.api.Account;
import com.compta.domains.api.Compta;
import com.compta.domains.api.OperationSens;
import com.compta.domains.api.TrameFluxDetail;
import com.compta.domains.api.TrameFluxDetailMetadata;
import com.compta.domains.api.TrameFluxDetailSimilaryAccounts;
import com.compta.domains.api.TrameFlux;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.securities.api.Tax;

/**
 * @author oob
 *
 */
public final class TrameFluxDetailDb extends GuidKeyEntityDb<TrameFluxDetail, TrameFluxDetailMetadata> implements TrameFluxDetail {
	
	private final Compta module;
	
	public TrameFluxDetailDb(final Base base, final UUID id, final Compta module){
		super(base, id, "Détail de flux de trame introuvable !");
		this.module = module;
	}
	
	@Override
	public Account generalAccount() throws IOException {
		UUID accountId = ds.get(dm.generalAccountIdKey());
		return new AccountDb(base, accountId, module);
	}

	@Override
	public OperationSens sens() throws IOException {
		int sensId = ds.get(dm.sensIdKey());
		return OperationSens.get(sensId);
	}

	@Override
	public TrameFlux flux() throws IOException {
		UUID fluxId = ds.get(dm.trameFluxIdKey());
		return new TrameFluxDb(base, fluxId, module);
	}
	
	public static void validate(Account generalAccount, OperationSens sens){
		if(generalAccount.isNone())
			throw new IllegalArgumentException("Vous devez renseigner le compte général associé !");
		
		if(sens == OperationSens.NONE)
			throw new IllegalArgumentException("Vous devez renseigner un sens (Débit ou Crédit) !"); 
	}

	@Override
	public boolean isAggregateAccount() throws IOException {
		return ds.get(dm.isAggregateAccountKey());
	}

	@Override
	public Formular formular() throws IOException {
		String expression = ds.get(dm.formular());
		return module.company().currency().calculator().withExpression(expression);
	}

	@Override
	public Map<String, Double> buildParams(Double amountBase) throws IOException {
		
		final String amountBaseKey = "{base}";
		
		Map<String, Double> params = new HashMap<String, Double>();
		List<Tax> taxes = flux().trame().pieceType().module().taxes().all();
		
		if(formular().params().containsKey(amountBaseKey))
			params.put(amountBaseKey, amountBase);
		
		for (Entry<String, Double> param : formular().params().entrySet()) {

			// taxes
			String operandeName = param.getKey().replace("{", "").replace("}", "").trim();
			
			Optional<Tax> optTax = taxes.stream().filter(m -> {
				try {
					return m.shortName().toLowerCase().equals(operandeName);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}).findFirst();
			
			if(!optTax.isPresent())
				params.put(param.getKey(), 0.0);
			else
				params.put(param.getKey(), optTax.get().decimalValue());
		}

		return params;
	}

	@Override
	public TrameFluxDetailSimilaryAccounts similaryAccounts() throws IOException {
		return new TrameFluxDetailSimilaryAccountsDb(base, this, module);
	}
}
