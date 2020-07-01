package com.lightpro.compta.rs;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.compta.domains.api.Account;
import com.compta.domains.api.Line;
import com.compta.domains.api.LineStatus;
import com.compta.domains.api.Reconcile;
import com.compta.domains.api.ReconcileStatus;
import com.compta.domains.api.Reconciles;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.PaginationSet;
import com.lightpro.compta.vm.CountVm;
import com.lightpro.compta.vm.ReconcileVm;
import com.securities.api.Secured;

@Path("/compta/reconcile")
public class ReconcileRs extends ComptaBaseRs {

	@GET
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSingle(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Reconcile item = compta().reconciles().get(id);

						return Response.ok(new ReconcileVm(item)).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response search( @QueryParam("page") int page, 
							@QueryParam("pageSize") int pageSize, 
							@QueryParam("filter") String filter,
							@QueryParam("tiersTypeId") UUID tiersTypeId,
							@QueryParam("tiersAccountId") UUID tiersAccountId,
							@QueryParam("statusId") int statusId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						TiersType tiersType = compta().tiersTypes().build(tiersTypeId);
						Account tiersAccount = compta().accounts().build(tiersAccountId);
						ReconcileStatus status = ReconcileStatus.get(statusId);
						
						Reconciles container = compta().reconciles().of(tiersType).of(tiersAccount).of(status);
						
						List<ReconcileVm> itemsVm = container.find(page, pageSize, filter)
														   .stream()
														   .map(m -> new ReconcileVm(m))
														   .collect(Collectors.toList());
													
						long count = container.count(filter);
						PaginationSet<ReconcileVm> pagedSet = new PaginationSet<ReconcileVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});					
	}
	
	@GET
	@Secured
	@Path("/complete/count")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getCompleteCount(@QueryParam("tiersTypeId") final UUID tiersTypeId) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						TiersType tiersType = compta().tiersTypes().build(tiersTypeId);
						long count = compta().reconciles().of(tiersType).of(ReconcileStatus.COMPLETE).count();

						return Response.ok(new CountVm(count)).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/partial/count")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPartialCount(@QueryParam("tiersTypeId") final UUID tiersTypeId) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						TiersType tiersType = compta().tiersTypes().build(tiersTypeId);
						long count = compta().reconciles().of(tiersType).of(ReconcileStatus.PARTIAL).count();

						return Response.ok(new CountVm(count)).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/payment/unreconciled/count")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getLineUnreconciledCount(@QueryParam("tiersTypeId") final UUID tiersTypeId) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						TiersType tiersType = compta().tiersTypes().build(tiersTypeId);
						long count = compta().lines().of(tiersType).of(LineStatus.UNRECONCILED).count();

						return Response.ok(new CountVm(count)).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Path("/{lineid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response create(@PathParam("lineid") final UUID lineId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Line line = compta().lines().get(lineId);
						Reconcile reconcile = compta().reconciles().of(line.auxiliaryAccount()).add();
																	
						reconcile.add(line);
						
						log.info(String.format("Création d'une correspondance %s"));
						return Response.ok(new ReconcileVm(reconcile)).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Path("/{id}/line/{lineid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addLine(@PathParam("id") final UUID id, @PathParam("lineid") final UUID lineId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Reconcile reconcile = compta().reconciles().get(id);
						Line line = compta().lines().get(lineId);
						
						reconcile.add(line);
						
						log.info(String.format("Ajout d'une ligne à une correspondance"));
						return Response.status(Status.OK).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Path("/{id}/lettrer")
	@Produces({MediaType.APPLICATION_JSON})
	public Response lettrer(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Reconcile reconcile = compta().reconciles().get(id);
						reconcile.lettrer();
						
						log.info(String.format("Lettrage de la correspondance"));
						return Response.status(Status.OK).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Path("/{id}/delettrer")
	@Produces({MediaType.APPLICATION_JSON})
	public Response delettrer(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Reconcile reconcile = compta().reconciles().get(id);
						reconcile.delettrer();
						
						log.info(String.format("Délettrage de la correspondance"));
						return Response.status(Status.OK).build();
					}
				});		
	}
	
	@DELETE
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response removeLine(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Reconcile item = compta().reconciles().get(id);						
						compta().reconciles().delete(item);
						
						log.info(String.format("Suppression de la correspondance"));
						return Response.status(Status.OK).build();
					}
				});		
	}
	
	@DELETE
	@Secured
	@Path("/{id}/line/{lineid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response removeLine(@PathParam("id") final UUID id, @PathParam("lineid") final UUID lineId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Reconcile reconcile = compta().reconciles().get(id);
						Line line = compta().lines().get(lineId);
						
						reconcile.remove(line);
						
						log.info(String.format("Suppression d'une ligne de la correspondance"));
						return Response.status(Status.OK).build();
					}
				});		
	}
}
