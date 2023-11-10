package br.com.doit.rxworld.service.doit;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.doit.quarkus.logging.LoggingFilter;
import br.com.doit.rxworld.service.doit.pojo.DOitProduct;
import br.com.doit.rxworld.service.doit.pojo.DOitSaleOrder;

@Path("/ra")
@RegisterRestClient
@RegisterProvider(LoggingFilter.class)
@RegisterProvider(DOitExceptionMapper.class)
@ClientHeaderParam(name = "Authorization", value = "{br.com.doit.rxworld.service.doit.DOitServiceHeaderGenerator.authorizationHeader}")
public interface DOitService {
  @GET
  @Path("/WebStores/{webStoreId}/Products/{sku}")
  DOitProduct getProduct(@PathParam("webStoreId") Integer webStoreId, @PathParam("sku") String sku);
  
  @POST
  @Path("/CommercialInvoice")
  DOitSaleOrder postSaleOrder(DOitSaleOrder saleOrder);
}