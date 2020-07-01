package com.lightpro.compta.rs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compta.domains.api.OperationSens;
import com.lightpro.compta.vm.ListValueVm;
import com.securities.api.Secured;

@Path("/compta/operation")
public class OperationRs extends ComptaBaseRs {
	@GET
	@Secured
	@Path("/type")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllTypes() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ListValueVm> items = Arrays.asList(OperationSens.values())
								 						.stream()
								 						.filter(m -> m.id() > 0) 
								 						.map(m -> new ListValueVm(m.id(), m.toString()))
								 						.collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
}
