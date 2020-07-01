package com.lightpro.compta.rs;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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

import com.common.utilities.convert.TimeConvert;
import com.compta.domains.api.Account;
import com.compta.domains.api.Flux;
import com.compta.domains.api.Journal;
import com.compta.domains.api.Piece;
import com.compta.domains.api.PieceArticle;
import com.compta.domains.api.PieceStatus;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Pieces;
import com.compta.domains.api.Tiers;
import com.infrastructure.core.PaginationSet;
import com.infrastructure.core.Period;
import com.infrastructure.core.impl.PeriodBase;
import com.lightpro.compta.cmd.FluxEdited;
import com.lightpro.compta.cmd.LineEdited;
import com.lightpro.compta.cmd.PieceArticleEdited;
import com.lightpro.compta.cmd.PieceEdited;
import com.lightpro.compta.vm.ListValueVm;
import com.lightpro.compta.vm.PieceVm;
import com.securities.api.Secured;

@Path("/compta/piece")
public class PieceRs extends ComptaBaseRs {
	
	@GET
	@Secured
	@Path("/status")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPieceStatus() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ListValueVm> items = Arrays.asList(PieceStatus.values())
													.stream()
													.map(m -> new ListValueVm(m.id(), m.toString()))
													.collect(Collectors.toList());

						return Response.ok(items).build();
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
						
						Piece item = compta().pieces().get(id);

						return Response.ok(new PieceVm(item)).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response search(@QueryParam("page") int page, 
						   @QueryParam("pageSize") int pageSize, 
						   @QueryParam("filter") String filter,
						   @QueryParam("typeId") UUID typeId,
						   @QueryParam("statusId") int statusId,
						   @QueryParam("start") Date start,
						   @QueryParam("end") Date end) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceType type = compta().pieceTypes().build(typeId);
						PieceStatus status = PieceStatus.get(statusId);
						LocalDate stateDate = TimeConvert.toLocalDate(start, ZoneId.systemDefault());
						LocalDate endDate = TimeConvert.toLocalDate(end, ZoneId.systemDefault());
						
						Period period = new PeriodBase(stateDate, endDate);						
						Pieces container = compta().pieces().of(type).of(status).of(period);
						
						List<PieceVm> itemsVm = container.find(page, pageSize, filter)
														   .stream()
														   .map(m -> new PieceVm(m))
														   .collect(Collectors.toList());
													
						long count = container.count(filter);
						PaginationSet<PieceVm> pagedSet = new PaginationSet<PieceVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});	
				
	}
	
	@POST
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response create(final PieceEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceType pieceType = compta().pieceTypes().get(cmd.typeId());
						Tiers tiers = compta().tiers().build(cmd.tiersId());
						
						Piece piece = pieceType.pieces().add(cmd.date(), cmd.reference(), cmd.origin(), cmd.notes(), cmd.dateEcheance(), tiers);
						
						for (PieceArticleEdited paCmd : cmd.articles()) {
							
							PieceArticle article = piece.articles().add();
							
							for (FluxEdited fluxCmd : paCmd.fluxes()) {
								Journal jrnl = compta().journals().build(fluxCmd.journalId());
								Flux flux = article.fluxes().add(fluxCmd.object(), fluxCmd.journalType(), jrnl);
								
								for (LineEdited line : fluxCmd.lines()) {
									Account generalAccount = compta().accounts().build(line.generalAccountId());						
									flux.lines().add(tiers, generalAccount, line.debit(), line.credit());
								}
							}	
						}						
						
						piece.validate();
						
						log.info(String.format("Création de la pièce %s N° %s", piece.type().name(), piece.reference()));
						return Response.ok(new PieceVm(piece)).build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") final UUID id, final PieceEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Piece piece = compta().pieces().get(id);
						Tiers tiers = compta().tiers().build(cmd.tiersId());
						
						piece.update(cmd.date(), cmd.reference(), cmd.origin(), cmd.notes(), cmd.dateEcheance(), tiers);
						
						if(piece.status() == PieceStatus.UNACCOUNTED) {
							piece.articles().deleteAll(); // reprendre la saisie
							
							for (PieceArticleEdited paCmd : cmd.articles()) {
								PieceArticle article = piece.articles().add();
								
								for (FluxEdited fluxCmd : paCmd.fluxes()) {
									Journal jrnl = compta().journals().build(fluxCmd.journalId());
									Flux flux = article.fluxes().add(fluxCmd.object(), fluxCmd.journalType(), jrnl);
									
									for (LineEdited line : fluxCmd.lines()) {
										Account generalAccount = compta().accounts().build(line.generalAccountId());										
										flux.lines().add(tiers, generalAccount, line.debit(), line.credit());
									}
								}
							}
						}
						
						piece.validate();
												
						log.info(String.format("Mise à jour des données de la pièce %s N° %s", piece.type().name(), piece.reference()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@POST
	@Path("/{id}/count")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response count(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Piece item = compta().pieces().get(id);
						item.count();
						
						log.info(String.format("Comptabilisation de la pièce %s N° %s", item.type().name(), item.reference()));
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
						
						Pieces items = compta().pieces();
						Piece item = items.get(id);
						String typeName = item.type().name();
						String reference = item.reference();
						items.delete(item);
						
						log.info(String.format("Suppression de la pièce %s N° %s", typeName, reference));
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
