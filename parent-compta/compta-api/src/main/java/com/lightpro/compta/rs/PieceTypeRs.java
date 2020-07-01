package com.lightpro.compta.rs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compta.domains.api.Account;
import com.compta.domains.api.Journal;
import com.compta.domains.api.OperationSens;
import com.compta.domains.api.PieceArticle;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.PieceTypes;
import com.compta.domains.api.TiersType;
import com.compta.domains.api.Trame;
import com.compta.domains.api.TrameFlux;
import com.compta.domains.api.TrameFluxDetail;
import com.compta.domains.api.Trames;
import com.infrastructure.core.PaginationSet;
import com.lightpro.compta.cmd.PieceTypeEdited;
import com.lightpro.compta.cmd.TrameDetailEdited;
import com.lightpro.compta.cmd.TrameDetailSimilaryAccountEdited;
import com.lightpro.compta.cmd.TrameEdited;
import com.lightpro.compta.cmd.TrameFluxEdited;
import com.lightpro.compta.cmd.TrameParamCmd;
import com.lightpro.compta.cmd.TrameParamDetailCmd;
import com.lightpro.compta.vm.JournalVm;
import com.lightpro.compta.vm.ListValueVm;
import com.lightpro.compta.vm.ParamVm;
import com.lightpro.compta.vm.PieceArticleVm;
import com.lightpro.compta.vm.PieceTypeVm;
import com.lightpro.compta.vm.TrameVm;
import com.securities.api.Secured;
import com.securities.api.Sequence;

@Path("/compta/piece-type")
public class PieceTypeRs extends ComptaBaseRs {
	
	@GET
	@Secured
	@Path("/nature")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPieceNatures() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ListValueVm> values = Arrays.asList(PieceNature.values())
								                         .stream()
								                         .filter(m -> m != PieceNature.NONE)
								                         .map(m -> new ListValueVm(m.id(), m.toString()))
								                         .collect(Collectors.toList());

						return Response.ok(values).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSingle(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceType item = compta().pieceTypes().get(id);

						return Response.ok(new PieceTypeVm(item)).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response searchAccountsOfActiveChart(@QueryParam("page") int page, 
												@QueryParam("pageSize") int pageSize, 
												@QueryParam("filter") String filter) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceTypes container = compta().pieceTypes();
						
						List<PieceTypeVm> itemsVm = container.find(page, pageSize, filter)
														   .stream()
														   .map(m -> new PieceTypeVm(m))
														   .collect(Collectors.toList());
													
						long count = container.count(filter);
						PaginationSet<PieceTypeVm> pagedSet = new PaginationSet<PieceTypeVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});	
				
	}
	
	@POST
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response create(final PieceTypeEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						TiersType type = compta().tiersTypes().build(cmd.tiersTypeManagedId());
						Sequence sequence = compta().sequences().build(cmd.sequenceId());
						PieceType item = compta().pieceTypes().add(cmd.name(), type, cmd.nature(), sequence, cmd.object());
						
						log.info(String.format("Création du type de pièce %s", item.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") final UUID id, final PieceTypeEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceType item = compta().pieceTypes().get(id);								
						TiersType type = compta().tiersTypes().build(cmd.tiersTypeManagedId());
						Sequence sequence = compta().sequences().build(cmd.sequenceId());
						Trame preferredtrame = item.trames().build(cmd.preferredTrameId());
						item.update(cmd.name(), type, cmd.nature(), sequence, cmd.object(), preferredtrame);
						
						log.info(String.format("Mise à jour des données du type de pièce %s", item.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@DELETE
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response delete(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceTypes items = compta().pieceTypes();
						PieceType item = items.get(id);
						String name = item.name();
						items.delete(item);
						
						log.info(String.format("Suppression du type de pièce %s", name));
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
	
	@POST
	@Secured
	@Path("{id}/generate")
	@Produces({MediaType.APPLICATION_JSON})
	public Response generate(@PathParam("id") final UUID id,
							 final TrameParamCmd cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceType type = compta().pieceTypes().get(id);
						Journal journal = compta().journals().get(cmd.journalId());
						
						Map<String, Double> params = new HashMap<String, Double>();
						for (TrameParamDetailCmd paramCmd : cmd.params()) {
							params.put(paramCmd.name(), paramCmd.value());
						}
						
						PieceArticle pieceArticle = type.trames().get(cmd.trameId()).generate(journal, params);
															
						log.info(String.format("Génération d'une pièce %s", type.name()));
						return Response.ok(new PieceArticleVm(pieceArticle)).build();
					}
				});	
				
	}
	
	@GET
	@Secured
	@Path("/{id}/journal")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getJournals(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<JournalVm> itemsVm = compta().pieceTypes().get(id).journals()
										.stream()
										.map(m -> new JournalVm(m))
										.collect(Collectors.toList());

						return Response.ok(itemsVm).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/{id}/trame")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getTrames(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<TrameVm> tramesVm = compta().pieceTypes().get(id).trames().all()
										.stream()
										.map(m -> new TrameVm(m))
										.collect(Collectors.toList());

						return Response.ok(tramesVm).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/{id}/trame/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response searchTrames(@PathParam("id") final UUID id,
												@QueryParam("page") int page, 
												@QueryParam("pageSize") int pageSize, 
												@QueryParam("filter") String filter) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceType pieceType = compta().pieceTypes().get(id);
						Trames container = pieceType.trames();
						
						List<TrameVm> itemsVm = container.find(page, pageSize, filter)
														   .stream()
														   .map(m -> new TrameVm(m))
														   .collect(Collectors.toList());
													
						long count = container.count(filter);
						PaginationSet<TrameVm> pagedSet = new PaginationSet<TrameVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});					
	}
	
	@GET
	@Secured
	@Path("/{id}/trame/{trameid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getTrame(@PathParam("id") final UUID id,
							 @PathParam("trameid") final UUID trameId) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Trame trame = compta().pieceTypes().get(id).trames().get(trameId);

						return Response.ok(new TrameVm(trame)).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/{id}/trame/{trameid}/param")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getTrameParams(@PathParam("id") final UUID id,
							 	   @PathParam("trameid") final UUID trameId) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Map<String, Double> params = compta().pieceTypes().get(id).trames().get(trameId)
													     .buildParams(0.0);

						List<ParamVm> paramsVm = new ArrayList<ParamVm>();
						for (Entry<String, Double> param : params.entrySet()) {
							paramsVm.add(new ParamVm(param.getKey(), param.getValue()));
						}
						
						return Response.ok(paramsVm).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Path("/{id}/trame")
	@Produces({MediaType.APPLICATION_JSON})
	public Response createTrame(@PathParam("id") final UUID id, final TrameEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceType pieceType = compta().pieceTypes().get(id);
						Trame trame = pieceType.trames().add(cmd.name());
						
						for (TrameFluxEdited trfCmd : cmd.fluxes()) {
							Journal defaultJournal = compta().journals().build(trfCmd.defaultJournalId());
							TrameFlux flux = trame.fluxes().add(trfCmd.description(), trfCmd.journalType(), defaultJournal);
							
							for (TrameDetailEdited trfDetail : trfCmd.details()) {
								Account generalAccount = compta().accounts().build(trfDetail.generalAccountId());
								OperationSens sens = trfDetail.sens();
								flux.details().add(generalAccount, sens, trfDetail.isAggregateAccount(), trfDetail.formular());
							}
						}
						
						trame.validate();
						
						log.info(String.format("Création de la trame %s", trame.name()));
						
						return Response.ok(new TrameVm(trame)).build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}/trame/{trameid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateTrame(@PathParam("id") final UUID id, 
			@PathParam("trameid") final UUID trameId,
			final TrameEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceType pieceType = compta().pieceTypes().get(id);
						Trame trame = pieceType.trames().get(trameId);
						
						trame.update(cmd.name()); 
						
						trame.fluxes().deleteAll();
						
						for (TrameFluxEdited trfCmd : cmd.fluxes()) {
							Journal defaultJournal = compta().journals().build(trfCmd.defaultJournalId());
							TrameFlux flux = trame.fluxes().add(trfCmd.description(), trfCmd.journalType(), defaultJournal);
							
							for (TrameDetailEdited trfDetail : trfCmd.details()) {
								Account generalAccount = compta().accounts().build(trfDetail.generalAccountId());
								OperationSens sens = trfDetail.sens();
								TrameFluxDetail detail = flux.details().add(generalAccount, sens, trfDetail.isAggregateAccount(), trfDetail.formular());
								for (TrameDetailSimilaryAccountEdited similaryAccountCmd : trfDetail.similaryAccounts()) {
									Account account = compta().accounts().build(similaryAccountCmd.id());
									detail.similaryAccounts().add(account);
								}								
							}
						}
						
						trame.validate();
						
						log.info(String.format("Mise à jour des données de la trame %s", trame.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@DELETE
	@Secured
	@Path("/{id}/trame/{trameid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deleteTrame(@PathParam("id") final UUID id,
			@PathParam("trameid") final UUID trameId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceType pieceType = compta().pieceTypes().get(id);
						Trame item = pieceType.trames().get(trameId);
						String name = item.name();
						pieceType.trames().delete(item);
						
						log.info(String.format("Suppression de la trame %s", name));
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
