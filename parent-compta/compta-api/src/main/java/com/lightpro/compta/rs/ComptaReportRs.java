package com.lightpro.compta.rs;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.compta.domains.api.Account;
import com.compta.domains.api.AccountNature;
import com.compta.domains.api.Exercise;
import com.compta.domains.api.Journal;
import com.compta.domains.api.TiersType;
import com.compta.reporting.report.api.ComptaReporting;
import com.compta.reporting.report.birt.BirtComptaReporting;
import com.infrastructure.core.Report;
import com.lightpro.compta.cmd.BalanceAgeeDesTiersReportParam;
import com.lightpro.compta.cmd.BalanceAuxiliaireReportParam;
import com.lightpro.compta.cmd.BalanceGeneraleReportParam;
import com.lightpro.compta.cmd.CompteResultatReportParam;
import com.lightpro.compta.cmd.GrandLivreReportParam;
import com.securities.api.Secured;

@Path("/compta/report")
public class ComptaReportRs extends ComptaBaseRs {
	
	protected ComptaReporting reporting() throws IOException {
		return new BirtComptaReporting(compta());
	}
	
	@POST
	@Secured
	@Path("/grand-livre")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response getGrandLivre(final GrandLivreReportParam cmd) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Journal journal = compta().journals().build(cmd.journalId()); // facultatif
						Report grandLivre = reporting().grandLivre(cmd.period(), journal);
			
						StreamingOutput fileStream = grandLivre.renderAsStreaming(cmd.format());
						
						log.info(String.format("Génération du grand-livre"));
						return Response.ok(fileStream).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Path("/balance-generale")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response getBalanceGenerale(final BalanceGeneraleReportParam cmd) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Report balanceGenerale = reporting().balanceGenerale(cmd.period());
			
						StreamingOutput fileStream = balanceGenerale.renderAsStreaming(cmd.format());
												
						log.info(String.format("Génération de la balance générale"));
						return Response.ok(fileStream).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Path("/balance-auxiliaire")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response getBalanceAuxiliaire(final BalanceAuxiliaireReportParam cmd) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						TiersType tiersType = compta().tiersTypes().get(cmd.tiersTypeId());
						Report balanceAuxiliaire = reporting().balanceAuxiliaire(tiersType, cmd.period());
						
						StreamingOutput fileStream = balanceAuxiliaire.renderAsStreaming(cmd.format());
												
						log.info(String.format("Génération de la balance auxiliaire"));
						return Response.ok(fileStream).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Path("/balance-agee-des-tiers")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response getBalanceAgeeDesTiers(final BalanceAgeeDesTiersReportParam cmd) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						TiersType tiersType = compta().tiersTypes().get(cmd.tiersTypeId());
						Account auxiliaryAccount = compta().accounts().of(AccountNature.AUXILIARY).build(cmd.auxiliaryAccountId());
						Report balanceAgeeDesTiers = reporting().balanceAgeeDesTiers(tiersType, auxiliaryAccount, cmd.startDate());
						
						StreamingOutput fileStream = balanceAgeeDesTiers.renderAsStreaming(cmd.format());
												
						log.info(String.format("Génération de la balance âgée des tiers"));
						return Response.ok(fileStream).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Path("/compte-resultat")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response getCompteResultat(final CompteResultatReportParam cmd) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Exercise exercise = compta().exercises().build(cmd.exerciseId());								
						Report compteResultat = reporting().compteResultat(exercise);
						
						StreamingOutput fileStream = compteResultat.renderAsStreaming(cmd.format());
												
						log.info(String.format("Génération du compte de résultat"));
						return Response.ok(fileStream).build();
					}
				});			
	}
}
