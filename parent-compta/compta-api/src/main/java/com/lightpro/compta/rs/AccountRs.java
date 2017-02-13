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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compta.domains.api.Account;
import com.compta.domains.api.AccountType;
import com.compta.domains.api.Accounts;
import com.lightpro.compta.cmd.AccountEdited;
import com.lightpro.compta.vm.AccountVm;
import com.lightpro.compta.vm.ListValueVm;
import com.securities.api.Secured;

@Path("/compta/account")
public class AccountRs extends ComptaBaseRs {
	
	@GET
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSingle(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Account item = compta().chart().accounts().get(id);

						return Response.ok(new AccountVm(item)).build();
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
						
						List<ListValueVm> items = Arrays.asList(AccountType.values())
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
	public Response create(@PathParam("id") final UUID id, final AccountEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						compta().chart().accounts().add(cmd.code(), cmd.name(), cmd.type());
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") final UUID id, final AccountEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Account item = compta().chart().accounts().get(id);
						item.update(cmd.code(), cmd.name(), cmd.type());
						
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
						
						Accounts items = compta().chart().accounts();
						Account item = items.get(id);
						items.delete(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
