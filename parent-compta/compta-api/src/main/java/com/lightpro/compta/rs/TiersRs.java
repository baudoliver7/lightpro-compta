package com.lightpro.compta.rs;

import java.io.IOException;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compta.domains.api.Account;
import com.compta.domains.api.TiersType;
import com.compta.domains.api.TiersTypes;
import com.lightpro.compta.cmd.TiersTypeEdited;
import com.lightpro.compta.vm.TiersTypeVm;
import com.securities.api.Secured;
import com.securities.api.Sequence;

@Path("/compta/tiers")
public class TiersRs extends ComptaBaseRs {
	
	@GET
	@Secured
	@Path("/type")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllTypes() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<TiersTypeVm> items = compta().tiersTypes().all()
								 						.stream()
								 						.map(m -> new TiersTypeVm(m))
								 						.collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@POST
	@Path("/type")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response addType(final TiersTypeEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {

						Account generalAccount = compta().accounts().build(cmd.generalAccountId());
						Sequence sequence = compta().sequences().build(cmd.sequenceId());
						Sequence sequenceLettrage = compta().sequences().build(cmd.sequenceLettrageId());
						TiersType tiersType = compta().tiersTypes().add(cmd.name(), generalAccount, sequence, sequenceLettrage);
						
						log.info(String.format("Création du type de tiers %s", tiersType.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Path("/type/{id}")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateType(@PathParam("id") final UUID id, final TiersTypeEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						TiersType type = compta().tiersTypes().get(id);
						Account generalAccount = compta().accounts().build(cmd.generalAccountId());
						Sequence sequence = compta().sequences().build(cmd.sequenceId());
						Sequence sequenceLettrage = compta().sequences().build(cmd.sequenceLettrageId());
						type.update(cmd.name(), generalAccount, sequence, sequenceLettrage);
						
						log.info(String.format("Mise à jour des données du type de tiers %s", type.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@DELETE
	@Secured
	@Path("/type/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response delete(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						TiersTypes items = compta().tiersTypes();
						TiersType item = items.get(id);
						String name = item.name();
						items.delete(item);
						
						log.info(String.format("Suppression du type de tiers %s", name));
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
