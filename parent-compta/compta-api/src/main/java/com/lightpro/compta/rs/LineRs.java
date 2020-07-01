package com.lightpro.compta.rs;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compta.domains.api.Account;
import com.compta.domains.api.LineStatus;
import com.compta.domains.api.Lines;
import com.compta.domains.api.PieceNature;
import com.compta.domains.api.TiersType;
import com.infrastructure.core.PaginationSet;
import com.lightpro.compta.vm.LineVm;
import com.securities.api.Secured;

@Path("/compta/line")
public final class LineRs extends ComptaBaseRs {

	@GET
	@Secured
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response search(@QueryParam("page") int page, 
						   @QueryParam("pageSize") int pageSize, 
						   @QueryParam("filter") String filter,
						   @QueryParam("pieceNatureId") int pieceNatureId,
						   @QueryParam("statusId") int statusId,
						   @QueryParam("auxiliaryAccountId") UUID auxiliaryAccountId,
						   @QueryParam("tiersTypeId") UUID tiersTypeId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PieceNature pieceNature = PieceNature.get(pieceNatureId);
						LineStatus status = LineStatus.get(statusId);
						Account auxiliaryAccount = compta().accounts().build(auxiliaryAccountId);
						TiersType tiersType = compta().tiersTypes().build(tiersTypeId);
				
						Lines container = compta().lines().of(pieceNature).of(status).ofAuxiliaryAccount(auxiliaryAccount).of(tiersType);
						
						List<LineVm> itemsVm = container.find(page, pageSize, filter)
														   .stream()
														   .map(m -> new LineVm(m))
														   .collect(Collectors.toList());
													
						long count = container.count(filter);
						PaginationSet<LineVm> pagedSet = new PaginationSet<LineVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});	
				
	}
}
