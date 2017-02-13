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

import com.compta.domains.api.Journal;
import com.compta.domains.api.JournalType;
import com.compta.domains.api.Journals;
import com.infrastructure.core.PaginationSet;
import com.lightpro.compta.cmd.JournalEdited;
import com.lightpro.compta.vm.JournalVm;
import com.lightpro.compta.vm.ListValueVm;
import com.securities.api.Secured;

@Path("/compta/journal")
public class JournalRs extends ComptaBaseRs {
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
													
						int count = container.totalCount(filter);
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
						
						compta().journals().add(cmd.code(), cmd.name(), cmd.type());
						
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
						
						Journals items = compta().journals();
						Journal item = items.get(id);
						items.delete(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
