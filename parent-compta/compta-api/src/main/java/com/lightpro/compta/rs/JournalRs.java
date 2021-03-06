package com.lightpro.compta.rs;

import java.io.IOException;
import java.util.Arrays;
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

import com.compta.domains.api.Account;
import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.Journals;
import com.compta.domains.api.PieceType;
import com.infrastructure.core.PaginationSet;
import com.lightpro.compta.cmd.JournalEdited;
import com.lightpro.compta.vm.JournalVm;
import com.lightpro.compta.vm.ListValueVm;
import com.lightpro.compta.vm.PieceTypeVm;
import com.securities.api.Secured;

@Path("/compta/journal")
public class JournalRs extends ComptaBaseRs {
	
	@GET
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAll(@QueryParam("typeId") final int typeId) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						JournalType type = JournalType.get(typeId);
						List<JournalVm> items = compta().journals().of(type).all()
								 						.stream()								 					
								 						.map(m -> new JournalVm(m))
								 						.collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@GET
	@Path("/bank-and-cash")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllBankAndCash() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<JournalVm> items = compta().journals().all()
								 						.stream()
								 						.filter(m -> {
															try {
																return m.type() == JournalType.BANQUE || m.type() == JournalType.LIQUIDITES;
															} catch (IOException e) {																
																e.printStackTrace();
																throw new IllegalArgumentException(e);
															}
														})
								 						.map(m -> new JournalVm(m))
								 						.collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@GET
	@Path("/sales")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllSales() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<JournalVm> items = compta().journals().all()
								 						.stream()
								 						.filter(m -> {
															try {
																return m.type() == JournalType.VENTE;
															} catch (IOException e) {																
																e.printStackTrace();
																throw new IllegalArgumentException(e);
															}
														})
								 						.map(m -> new JournalVm(m))
								 						.collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@GET
	@Path("/od")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllOd() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<JournalVm> items = compta().journals().all()
								 						.stream()
								 						.filter(m -> {
															try {
																return m.type() == JournalType.DIVERS;
															} catch (IOException e) {																
																e.printStackTrace();
																throw new IllegalArgumentException(e);
															}
														})
								 						.map(m -> new JournalVm(m))
								 						.collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@GET
	@Path("/dashboard")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllDashboard() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<JournalVm> items = compta().journals().all()
								 						.stream()
								 						.filter(m -> m.isViewedOnDashboard())
								 						.map(m -> new JournalVm(m))
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
						
						Journal item = compta().journals().get(id);

						return Response.ok(new JournalVm(item)).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/{id}/piece-type")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPieceTypes(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Journal jnl = compta().journals().get(id);
						List<PieceTypeVm> itemsVm = jnl.pieceTypes()
													   .stream()
													   .map(m -> new PieceTypeVm(m))
													   .collect(Collectors.toList());
						
						return Response.ok(itemsVm).build();
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
						
						Journals container = compta().journals();
						
						List<JournalVm> itemsVm = container.find(page, pageSize, filter)
														   .stream()
														   .map(m -> new JournalVm(m))
														   .collect(Collectors.toList());
													
						long count = container.count(filter);
						PaginationSet<JournalVm> pagedSet = new PaginationSet<JournalVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});	
				
	}
	
	@GET
	@Secured
	@Path("/type")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllTypes() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ListValueVm> items = Arrays.asList(JournalType.values())
								 						.stream()
								 						.filter(m -> m.id() > 0) 
								 						.map(m -> new ListValueVm(m.id(), m.toString()))
								 						.collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response create(@PathParam("id") final UUID id, final JournalEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Account account = compta().accounts().build(cmd.accountId());
						Journal item = compta().journals().add(cmd.code(), cmd.name(), cmd.type(), account, cmd.validateAccount());
						
						item.viewOnDashboard(cmd.isViewedOnDashboard());
						
						log.info(String.format("Cr�ation du journal %s", item.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@POST
	@Path("/{id}/piece-type/{typeid}")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response addPiece(@PathParam("id") final UUID id, @PathParam("typeid") final UUID typeId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Journal jnl = compta().journals().get(id);
						PieceType pieceType = compta().pieceTypes().build(typeId);
						
						jnl.addPieceType(pieceType);
						
						log.info(String.format("Ajout du type de pi�ce %s au journal %s", pieceType.name(), jnl.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") final UUID id, final JournalEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Journal item = compta().journals().get(id);
						Account account = compta().accounts().build(cmd.accountId());
						item.update(cmd.code(), cmd.name(), cmd.type(), account, cmd.validateAccount());
						
						item.viewOnDashboard(cmd.isViewedOnDashboard());
						
						log.info(String.format("Mise � jour des donn�es du journal %s", item.name()));
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
						
						Journals items = compta().journals();
						Journal item = items.get(id);
						String name = item.name();
						items.delete(item);
						
						log.info(String.format("Suppression du journal %s", name));
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
	
	@DELETE
	@Path("/{id}/piece-type/{typeid}")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response removePiece(@PathParam("id") final UUID id, @PathParam("typeid") final UUID typeId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Journal jnl = compta().journals().get(id);
						PieceType pieceType = compta().pieceTypes().build(typeId);
						
						jnl.removePieceType(pieceType);
						
						log.info(String.format("Retrait du type de pi�ce %s du journal %s", pieceType.name(), jnl.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
}
