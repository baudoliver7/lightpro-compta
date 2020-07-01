package com.compta.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.compta.domains.api.Account;
import com.compta.domains.api.Flux;
import com.compta.domains.api.Journal;
import com.compta.domains.api.LineStatus;
import com.compta.domains.api.Line;
import com.compta.domains.api.Lines;
import com.compta.domains.api.OperationSens;
import com.compta.domains.api.Piece;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.PieceStatus;
import com.compta.domains.api.Tiers;
import com.compta.domains.api.TiersType;
import com.compta.domains.api.TrameFluxDetail;
import com.compta.domains.api.TrameFlux;
import com.infrastructure.core.Period;

public final class LinesTemplate implements Lines {

	private transient final TrameFlux trameFlux;
	private transient final Journal journal;
	private transient Flux flux;
	private final transient Map<String, Double> params;
	private final transient boolean reverse;
	
	public LinesTemplate(TrameFlux trameFlux, Journal journal, Map<String, Double> params){
		this(trameFlux, journal, params, false);
	}
	
	public LinesTemplate(TrameFlux trameFlux, Journal journal, Map<String, Double> params, boolean reverse){
		this.trameFlux = trameFlux;
		this.journal = journal;
		this.params = params;
		this.reverse = reverse;
	}
	
	private Flux flux() throws IOException {
		if(flux == null)
			flux = reverse ? trameFlux.generateReverse(journal, params) : trameFlux.generate(journal, params);
		
		return flux;
	}
	
	@Override
	public List<Line> find(String filter) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public List<Line> find(int page, int pageSize, String filter) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public List<Line> all() throws IOException {
		List<Line> lines = new ArrayList<Line>();
		
		for (TrameFluxDetail detail : trameFlux.details().all()) {				
			
			Account generalAccount = detail.generalAccount();
			if(detail.isAggregateAccount() && !journal.account().isNone())
				generalAccount = journal.account();
			
			OperationSens sens = detail.sens();
			if(reverse){
				if(sens == OperationSens.DEBIT)
					sens = OperationSens.CREDIT;
				else
					sens = OperationSens.DEBIT;
			}
			
			Line line = new LineTemplate(generalAccount, sens, flux(), detail.formular(), params);
			if(!line.isNull())
				lines.add(line);
		}		
		
		return lines;
	}

	@Override
	public Line build(UUID id) {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public boolean contains(Line item) {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Line get(UUID id) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public void delete(Line item) throws IOException {
		
	}

	@Override
	public Line add(Tiers tiers, Account generalAccount, double debit, double credit) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public void deleteAll() throws IOException {
		
	}

	@Override
	public void validate() throws IOException {
		
	}

	@Override
	public Lines of(Journal journal) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Lines of(Piece piece) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Lines of(PieceStatus pieceStatus) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Lines of(Period pieceDatePeriod) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Lines of(PieceNature pieceNature) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Lines of(LineStatus status) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Lines withReference(String reference) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Lines withOrigin(String origin) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Lines ofGeneralAccount(Account account) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Lines ofAuxiliaryAccount(Account account) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Lines of(TiersType tiersType) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Lines of(Flux flux) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public double debit() throws IOException {
		return 0;
	}

	@Override
	public double credit() throws IOException {
		return 0;
	}

	@Override
	public double balance() throws IOException {
		return 0;
	}

	@Override
	public long count(String filter) throws IOException {
		return all().size();
	}

	@Override
	public long count() throws IOException {
		return all().size();
	}

	@Override
	public Line first() throws IOException {
		return all().get(0);
	}

	@Override
	public boolean isEmpty() {
		try {
			return all().isEmpty();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Line last() throws IOException {
		int size = all().size();
		return all().get(size - 1); 
	}
}
