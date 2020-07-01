package com.lightpro.compta.rs;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compta.domains.api.Compta;
import com.compta.domains.impl.ComptaDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.pgsql.PgBase;
import com.lightpro.compta.cmd.JournalRanEdited;
import com.lightpro.compta.cmd.YearDayEdited;
import com.lightpro.compta.vm.JournalVm;
import com.lightpro.compta.vm.YearDayVm;
import com.securities.api.Company;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.securities.api.Secured;
import com.securities.impl.CompanyDb;

@Path("/compta/module")
public final class ComptaModuleRs extends ComptaBaseRs {
	
	@GET
	@Secured
	@Path("/last-day-exo")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getLastDayExo() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
											
						return Response.ok(new YearDayVm(compta().lastMonthExo(), compta().lastDayExo())).build();	
					}
				});					
	}
	
	@POST
	@Path("/last-day-exo")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateLastDayExo(final YearDayEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						compta().updateLastDayExo(cmd.month(), cmd.day());
												
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/journal-ran")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getJournalRan() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
											
						return Response.ok(new JournalVm(compta().journalRan())).build();	
					}
				});					
	}
	
	@POST
	@Path("/journal-ran")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateJournalRan(final JournalRanEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						compta().updateJournalRan(compta().journals().build(cmd.id()));
												
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Path("/install")
	@Produces({MediaType.APPLICATION_JSON})
	public Response installModule() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
											
						Module compta = compta(currentCompany.modulesSubscribed().get(ModuleType.COMPTA));					
						compta.install();
												
						return Response.status(Response.Status.OK).build();	
					}
				});					
	}
	
	@POST
	@Secured
	@Path("/uninstall")
	@Produces({MediaType.APPLICATION_JSON})
	public Response uninstallModule() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
											
						compta().uninstall();
												
						return Response.status(Response.Status.OK).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Path("/{companyId}/uninstall")
	@Produces({MediaType.APPLICATION_JSON})
	public Response uninstallModule(@PathParam("companyId") final UUID companyId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
							
						Base base = PgBase.getInstance(currentUser.id(), companyId);
						
						try {
											
							Company company = new CompanyDb(base, companyId);
							Module module = company.modulesInstalled().get(ModuleType.COMPTA);
							Compta compta = new ComptaDb(base, module);
							
							compta.uninstall();
							
							base.commit();
						} catch (IllegalArgumentException e) {
							base.rollback();
							throw e;
						} 
						catch (Exception e) {
							base.rollback();
							throw e;
						} finally {
							base.terminate();
						}						
						
						return Response.status(Response.Status.OK).build();
					}
				});			
	}
}
