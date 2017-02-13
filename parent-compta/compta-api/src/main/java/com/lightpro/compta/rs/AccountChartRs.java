package com.lightpro.compta.rs;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compta.domains.api.Accounts;
import com.infrastructure.core.PaginationSet;
import com.lightpro.compta.vm.AccountChartVm;
import com.lightpro.compta.vm.AccountVm;
import com.securities.api.Secured;

@Path("/compta/chart")
public class AccountChartRs extends ComptaBaseRs {
	
	@GET
	@Secured
	@Path("/active")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getActiveChart() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						AccountChartVm item = new AccountChartVm(compta().chart());

						return Response.ok(item).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/active/account/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response searchAccountsOfActiveChart(@QueryParam("page") int page, 
												@QueryParam("pageSize") int pageSize, 
												@QueryParam("filter") String filter) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Accounts container = compta().chart().accounts();
						
						List<AccountVm> itemsVm = container.find(page, pageSize, filter)
														   .stream()
														   .map(m -> new AccountVm(m))
														   .collect(Collectors.toList());
													
						int count = container.totalCount(filter);
						PaginationSet<AccountVm> pagedSet = new PaginationSet<AccountVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});	
				
	}
}
