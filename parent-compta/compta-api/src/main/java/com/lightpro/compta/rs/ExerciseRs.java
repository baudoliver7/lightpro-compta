package com.lightpro.compta.rs;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
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
import com.compta.domains.api.Exercise;
import com.compta.domains.api.Exercises;
import com.infrastructure.core.PaginationSet;
import com.infrastructure.core.impl.PeriodBase;
import com.lightpro.compta.cmd.ExerciseEdited;
import com.lightpro.compta.vm.ExerciseVm;
import com.securities.api.Secured;

@Path("/compta/exercise")
public class ExerciseRs extends ComptaBaseRs {
	
	@GET
	@Secured
	@Path("/current")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getCurrentExercise() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
											
						return Response.ok(new ExerciseVm(compta().exercises().current())).build();	
					}
				});					
	}
	
	@GET
	@Path("/all")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllExercises() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
												
						List<ExerciseVm> itemsVm = compta().exercises().all()
													.stream()
													.map(m -> new ExerciseVm(m))
													.collect(Collectors.toList());
						
						return Response.ok(itemsVm).build();	
					}
				});					
	}
	
	@GET
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getExercise(@QueryParam("date") final Date date) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						LocalDate localDate = TimeConvert.toLocalDate(date, ZoneId.systemDefault());
						Exercise exo = compta().exercises().get(localDate);
						
						if(exo.isNone())
							throw new IllegalArgumentException("Aucun exercice n'est défini à cette date !");
						
						return Response.ok(new ExerciseVm(exo)).build();	
					}
				});					
	}
	
	@GET
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getExercise(@QueryParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Exercise exo = compta().exercises().get(id);
						
						return Response.ok(new ExerciseVm(exo)).build();	
					}
				});					
	}
	
	@GET
	@Secured
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response search( @QueryParam("page") int page, 
							@QueryParam("pageSize") int pageSize, 
							@QueryParam("filter") String filter) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Exercises container = compta().exercises();
						
						List<ExerciseVm> itemsVm = container.find(page, pageSize, filter)
														   .stream()
														   .map(m -> new ExerciseVm(m))
														   .collect(Collectors.toList());
													
						long count = container.count(filter);
						PaginationSet<ExerciseVm> pagedSet = new PaginationSet<ExerciseVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});	
				
	}
	
	@POST
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response create(final ExerciseEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						compta().exercises().open(new PeriodBase(cmd.start(), cmd.end()));
						
						log.info(String.format("Création d'un exercice"));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@POST
	@Path("open-next")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response createNext() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						compta().exercises().openNext();
						
						log.info(String.format("Création de l'exercice suivant"));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@POST
	@Path("/{id}/close")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response closeExo(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Exercise exo = compta().exercises().get(id);
						exo.close();
						
						log.info(String.format("Clôture d'un exercice"));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") final UUID id, final ExerciseEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Exercise item = compta().exercises().get(id);
						item.update(new PeriodBase(cmd.start(), cmd.end()));

						log.info(String.format("Mise à jour d'un exercice"));
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
						
						Exercises items = compta().exercises();
						Exercise item = items.get(id);
						items.delete(item);
						
						log.info(String.format("Suppresion d'un exercice"));
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
