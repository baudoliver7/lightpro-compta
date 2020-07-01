package com.lightpro.compta.rs;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compta.domains.api.Journal;
import com.compta.domains.api.PieceType;
import com.compta.domains.api.Trame;
import com.compta.domains.impl.TrameNone;
import com.compta.interfacage.sales.api.InvoiceInterface;
import com.compta.interfacage.sales.api.ModulePdvInterface;
import com.compta.interfacage.sales.api.PaymentModeInterface;
import com.compta.interfacage.sales.api.PaymentInterface;
import com.compta.interfacage.sales.api.PdvPaymentModeInterface;
import com.compta.interfacage.sales.api.ProductCategoryInterface;
import com.lightpro.compta.cmd.FacturationSaleInterfaceEdited;
import com.lightpro.compta.cmd.ModulePdvInterfaceEdited;
import com.lightpro.compta.cmd.PaymentModeSaleInterfaceEdited;
import com.lightpro.compta.cmd.PaymentSaleInterfaceEdited;
import com.lightpro.compta.cmd.PdvPaymentModeSaleInterfaceEdited;
import com.lightpro.compta.cmd.ProductCategorySaleInterfaceEdited;
import com.lightpro.compta.vm.InvoiceInterfaceVm;
import com.lightpro.compta.vm.ModulePdvInterfaceVm;
import com.lightpro.compta.vm.PaymentInterfaceVm;
import com.lightpro.compta.vm.ProductCategoryInterfaceVm;
import com.securities.api.Secured;

@Path("/compta/interfacage")
public class InterfacageRs extends ComptaBaseRs {
	
	// facturation - ventes
	@GET
	@Secured
	@Path("/sales/facturation")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getFacturationSale() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						InvoiceInterface invoiceInterface = compta().interfacage().salesInterface().invoiceInterface();					
						return Response.ok(new InvoiceInterfaceVm(invoiceInterface)).build();	
					}
				});					
	}
	
	@GET
	@Secured
	@Path("/sales/facturation/product-category")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getProductCategoriesSale() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ProductCategoryInterfaceVm> itemsVm = compta().interfacage().salesInterface()
								                                           .invoiceInterface()
																		   .productCategories()
																		   .all()
																		   .stream()
																		   .map(m -> new ProductCategoryInterfaceVm(m))
																		   .collect(Collectors.toList());
						
						return Response.ok(itemsVm).build();	
					}
				});					
	}
	
	@POST
	@Path("/sales/facturation")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateFacturationSale(final FacturationSaleInterfaceEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						InvoiceInterface invoiceInterface = compta().interfacage().salesInterface().invoiceInterface();
						Journal journalVente = compta().journals().build(cmd.journalVenteId());
						PieceType factureClient = compta().pieceTypes().build(cmd.factureClientId());
						
						Trame factureDoitTrame = new TrameNone();
						if(!factureClient.isNone()){
							factureDoitTrame = factureClient.trames().build(cmd.factureDoitTrameId());
						}
						
						invoiceInterface.update(journalVente, factureClient, factureDoitTrame);
						
						// mettre à jour les catégories de produits modifiés
						for (ProductCategorySaleInterfaceEdited pcCmd : cmd.productCategories()) {
							ProductCategoryInterface item = invoiceInterface.productCategories().get(pcCmd.id());
							
							journalVente = compta().journals().build(pcCmd.journalVenteId());
							factureClient = compta().pieceTypes().build(pcCmd.factureClientId());
							
							factureDoitTrame = new TrameNone();
							
							if(!factureClient.isNone()){
								factureDoitTrame = factureClient.trames().build(pcCmd.factureDoitTrameId());
							}
							
							item.update(journalVente, factureClient, factureDoitTrame);
						}
						
						log.info(String.format("Configuration des paramètres d'interfaçage des factures de la vente"));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	// paiement - ventes
	@GET
	@Secured
	@Path("/sales/payment")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPaymentSale() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						PaymentInterface payment = compta().interfacage().salesInterface().paymentInterface();					
						return Response.ok(new PaymentInterfaceVm(payment)).build();	
					}
				});					
	}
	
	@POST
	@Path("/sales/payment")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response updatePaymentSale(final PaymentSaleInterfaceEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PaymentInterface payment = compta().interfacage().salesInterface().paymentInterface();

						for (PaymentModeSaleInterfaceEdited pmCmd : cmd.paymentModes()) {
							PaymentModeInterface item = payment.paymentModes().get(pmCmd.id());
							
							Journal journalEncaissement = compta().journals().build(pmCmd.journalEncaissementId());
							PieceType reglement = compta().pieceTypes().build(pmCmd.reglementId());
							
							Trame acompteOrAvance = new TrameNone();
							Trame reglementDefinitif = new TrameNone();
							Trame provision = new TrameNone();
							
							if(!reglement.isNone()){
								acompteOrAvance = reglement.trames().build(pmCmd.acompteOrAvanceId());
								reglementDefinitif = reglement.trames().build(pmCmd.reglementDefinitifId());
								provision = reglement.trames().build(pmCmd.provisionId());
							}
							
							item.update(journalEncaissement, reglement, acompteOrAvance, reglementDefinitif, provision);
						}
						
						log.info(String.format("Configuration des paramètres d'interfaçage des paiements directes"));
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	// point de vente - ventes
	@GET
	@Secured
	@Path("/sales/module-pdv")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getModulePdvs() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ModulePdvInterfaceVm> itemsVm = compta().interfacage()
								                                     .salesInterface()
																	 .pdvInterface()
																	 .modulePdvInterfaces()
																	 .all()
																     .stream()
																     .map(m -> new ModulePdvInterfaceVm(m))
																     .collect(Collectors.toList());
						
						return Response.ok(itemsVm).build();	
					}
				});					
	}
	
	@PUT
	@Path("/sales/module-pdv/{id}")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateModulePdv(@PathParam("id") final UUID id, final ModulePdvInterfaceEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						ModulePdvInterface pdv = compta().interfacage().salesInterface().pdvInterface().modulePdvInterfaces().get(id);

						for (PdvPaymentModeSaleInterfaceEdited pmCmd : cmd.paymentModes()) {
							PdvPaymentModeInterface item = pdv.paymentModes().get(pmCmd.id());
							
							Journal journalEncaissement = compta().journals().build(pmCmd.journalEncaissementId());
							PieceType reglement = compta().pieceTypes().build(pmCmd.reglementId());
							
							Trame acompteOrAvance = new TrameNone();
							Trame reglementDefinitif = new TrameNone();
							Trame provision = new TrameNone();
							
							if(!reglement.isNone()){
								acompteOrAvance = reglement.trames().build(pmCmd.acompteOrAvanceId());
								reglementDefinitif = reglement.trames().build(pmCmd.reglementDefinitifId());
								provision = reglement.trames().build(pmCmd.provisionId());
							}
							
							item.update(journalEncaissement, reglement, acompteOrAvance, reglementDefinitif, provision);
						}
						
						log.info(String.format("Configuration des paramètres d'interfaçage des paiements faits dans un point de vente"));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
}
